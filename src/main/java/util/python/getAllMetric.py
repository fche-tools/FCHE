import sys
import csv
import xml.dom.minidom as minidom
import os
import re


# python replaceAll
def replaceAll(input_string, to_replace, replace_with):
    while input_string.find(to_replace) > -1:
        input_string = input_string.replace(to_replace, replace_with)
    return input_string


# 给定bug report的xml文件 返回所有的bugId的集合keySet(bug_id)
def getBugIdSet(xml_path):
    xmlfilepath = os.path.abspath(xml_path)
    # 获取文件对象
    doc = minidom.parse(xmlfilepath)
    # 获取元素对象
    root = doc.documentElement
    # 获取子标签
    items = root.getElementsByTagName("item")
    # 将所有的key放入集合中
    bug_id_set = set()
    for item in items:
        key = item.getElementsByTagName("key")[0].childNodes[0].nodeValue
        bug_id_set.add(key)
    return bug_id_set


# 根据bug_id得到bug_name
def getBugName(bug_id_set):
    bug_name = ''
    for id in bug_id_set:
        if id != '' and id.find('-'):
            bug_name = id[:id.find('-')]
            return bug_name
    return bug_name


# 通过log得到commit_id与bug_id以及author的KV  返回KV{commit_id:[bug_id,author]}
def getCommitBugKV(log_path, bug_name):
    if(bug_name == ''):
        raise RuntimeError('no bug name!! check out if bug name like AVRO-1234, if not ,can not find bug!')
    with open(log_path, 'r', encoding='utf-8') as fr:
        text = fr.read()

        id_bug_kv = {}
        for i in text.split('commit'):
            if i != '' and re.search(bug_name + '-[\d]*', i, flags=0):  # AVRO-XXX WW-XXX
                commit_id = i[1:41]
                bug_id = re.search(bug_name + '-[\d]*', i, flags=0).group(0)

                if not i.find("Author"):
                    continue
                start = i.find("Author")
                authors = i[start + 8:]
                author = authors[:authors.find(' ')]
                id_bug_kv[commit_id] = [bug_id, author]
        return id_bug_kv


# 将methodChange.csv中的数据提取，得到methodDb:KV{methodName:[{commit_id:[Statement_insert,Statement_delete]}]}
def getMethodDb(method_change_path):
    method_db = {}

    # 将methodChange.csv中的数据提取，得到methodDb
    with open(method_change_path, "r", encoding='GBK', errors='ignore') as f:  # windows-1252 GBK
        reader = csv.reader((line.replace('\0', '') for line in f))

        for metric in reader:
            if metric.__contains__("CommitId"):
                continue

            if metric.__len__() < 5:
                continue
            commit_id = metric[0]
            change_type = metric[4]
            root_entity = metric[5]
            root_name = replaceAll(metric[6], ',', ' ')
            # root_name = root_name[0:root_name.rfind('(')]

            if root_entity == "METHOD":
                if change_type == "STATEMENT_INSERT":
                    if root_name in method_db:  # 名字在Db里
                        if commit_id in method_db.get(root_name):  # commitId在Db里
                            method_db.get(root_name).get(commit_id)[0] += 1
                        else:  # commitId不在Db里
                            method_db.get(root_name)[commit_id] = [1, 0]
                    else:  # 名字不在Db
                        method_db[root_name] = {commit_id: [1, 0]}

                elif change_type == "STATEMENT_DELETE":
                    if root_name in method_db:  # 名字在Db里
                        if commit_id in method_db.get(root_name):  # commitId在Db里
                            method_db.get(root_name).get(commit_id)[1] += 1
                        else:  # commitId不在Db里
                            method_db.get(root_name)[commit_id] = [0, 1]
                    else:  # 名字不在Db
                        method_db[root_name] = {commit_id: [0, 1]}

                elif change_type == "STATEMENT_UPDATE" \
                        or change_type == "STATEMENT_ORDERING_CHANGE" \
                        or change_type == "STATEMENT_PARENT_CHANGE":
                    if root_name in method_db:  # 名字在Db里
                        if commit_id in method_db.get(root_name):  # commitId在Db里
                            method_db.get(root_name).get(commit_id)[0] += 1
                        else:  # commitId不在Db里
                            method_db.get(root_name)[commit_id] = [1, 0]
                    else:  # 名字不在Db
                        method_db[root_name] = {commit_id: [1, 0]}
                else:
                    if root_name not in method_db:  # 名字不在Db里
                        method_db[root_name] = {commit_id: [0, 0]}

    return method_db


# 将methodMetric.csv导入（Paser导出）
# 导出Method Churn count历史修改次数，Churned num增加修改stmt数，DeleteNum删除stmt数，Churned num/Total LOC， Deleted num/Total LOC Churned num/Deleted num, Nums worked on/Churn count, authorNum, bug-prone
# metricKV={metricName:[paraCount,totalLOC,Churn count, Churned num, DeleteNum, Churned num/Total LOC,
# Deleted num/Total LOC, Churned num/Deleted num, Nums worked on/Churn count, authorNum, bug-prone]}
def getChurn(method_metric_path, methodDb, bugIdSet, commitBugKV):
    # 打开metric.csv
    metricKV = {}
    with open(method_metric_path, "r") as f:
        reader = csv.reader(f)

        for line in reader:
            if line[0] == "id":
                continue
            methodHistory = 0
            # stmt添加
            stmtAdd = 0
            # stmt删除
            stmtDel = 0
            # bug数
            bugNum = 0
            churnNumPerLOC = 0
            deleteNumPerLOC = 0
            addPerDel = 0
            numsWorkedOnPerCount = 0

            author_set = {}

            # paraCount totalLOC
            paraCount = int(line[2])
            totalLOC = int(line[3])

            metricList = []

            # 类名修改需根据项目改动
            if line[1].__contains__("java/org/") or line[1].__contains__("java/com/"):
                if line[1].__contains__("java/org/"):
                    methodName = replaceAll(line[1][line[1].find("java/org/") + 5:], '/', '.')
                elif line[1].__contains__("java/com/"):
                    methodName = replaceAll(line[1][line[1].find("java/com/") + 5:], '/', '.')

                # 当metric里找到change里对应的metric时
                if methodName in methodDb:
                    commitKV = methodDb.get(methodName)
                    methodHistory = commitKV.__len__()

                    # 处理commit信息
                    for commit in commitKV:
                        if commit in commitBugKV:
                            if commitBugKV[commit][0] in bugIdSet:
                                bugNum += 1
                            author_set[commitBugKV[commit][1]] = ''
                            stmtAdd += commitKV[commit][0]
                            stmtDel += commitKV[commit][1]

            if not totalLOC == 0:
                churnNumPerLOC = stmtAdd / totalLOC
                deleteNumPerLOC = stmtDel / totalLOC

            if not stmtDel == 0:
                addPerDel = stmtAdd / stmtDel
            if not methodHistory == 0:
                numsWorkedOnPerCount = (stmtAdd + stmtDel) / methodHistory
            metricList.append(paraCount)
            metricList.append(totalLOC)
            metricList.append(methodHistory)
            metricList.append(stmtAdd)
            metricList.append(stmtDel)
            metricList.append(churnNumPerLOC)
            metricList.append(deleteNumPerLOC)
            metricList.append(addPerDel)
            metricList.append(numsWorkedOnPerCount)
            metricList.append(author_set.__len__())
            if bugNum != 0:
                bugNum = "bug-prone"
            else:
                bugNum = "not bug-prone"
            metricList.append(bugNum)

            # 去除不存在历史修改的文件
            if methodHistory > 0:
                method_name = line[1]
                method_name = replaceAll(method_name[(method_name.find('/java/org')+6):(method_name.rfind('('))], '/', '.')
                metricKV[method_name] = metricList
    return metricKV


# 给定understand导出文件路径，返回methodMetric={methodName：["CountInput", "CountLine", "CountLineBlank", "CountLineCode",
# "CountLineCodeDecl", "CountLineCodeExe", "CountLineComment", "CountOutput", "CountPath", "CountStmt", "CountStmtDecl",
# "CountStmtExe", "Cyclomatic", "MaxNesting"]}
def getMethodMetric(understand_output_path):
    method_metric_kv = {}
    with open(understand_output_path, "r", encoding='utf-8')as f:
        reader = csv.reader((line.replace('\0', '') for line in f))
        for metric in reader:
            if (metric[2].endswith(".java") and metric[0].__contains__('Method') and not metric[0].__contains__(
                    'Unknown')):
                method_metric_kv[metric[1]] = metric[3:17]
    return method_metric_kv


# 给定methodChange.csv的路径 返回methodDB = {methodName:[statementModify,expressionModify,commentModify,returnTypeModify,
# parameterModify,prefixModify]}
def getMetricHistory(method_change_path):
    methodDb = {}
    # 将methodChange.csv中的数据提取，得到methodDb
    with open(method_change_path, "r", encoding='windows-1252', errors='ignore') as f:  # windows-1252 GBK
        reader = csv.reader((line.replace('\0', '') for line in f))

        for metric in reader:
            if metric.__contains__("CommitId"):
                continue

            if metric.__len__() < 5:
                continue
            changeType = metric[4]
            rootEntity = metric[5]
            rootName = replaceAll(metric[6], ',', ' ')
            rootName = rootName[0:rootName.rfind('(')]

            if rootEntity == "METHOD":
                # statement
                if changeType == "STATEMENT_INSERT" \
                        or changeType == "STATEMENT_DELETE" \
                        or changeType == "STATEMENT_UPDATE" \
                        or changeType == "STATEMENT_ORDERING_CHANGE":
                    if rootName in methodDb:  # 名字在Db里
                        methodDb.get(rootName)[0] += 1
                    else:  # 名字不在Db
                        methodDb[rootName] = [1, 0, 0, 0, 0, 0]
                # expression
                elif changeType == "CONDITION_EXPRESSION_CHANGE" \
                        or changeType == "ALTERNATIVE_PART_INSERT" \
                        or changeType == "ALTERNATIVE_PART_DELETE":
                    if rootName in methodDb:  # 名字在Db里
                        methodDb.get(rootName)[1] += 1
                    else:  # 名字不在Db
                        methodDb[rootName] = [0, 1, 0, 0, 0, 0]
                # comment
                elif changeType == "COMMENT_INSERT" \
                        or changeType == "COMMENT_MOVE" \
                        or changeType == "COMMENT_DELETE" \
                        or changeType == "COMMENT_UPDATE":
                    if rootName in methodDb:  # 名字在Db里
                        methodDb.get(rootName)[2] += 1
                    else:  # 名字不在Db
                        methodDb[rootName] = [0, 0, 1, 0, 0, 0]
                # returnType
                elif changeType == "RETURN_TYPE_CHANGE" \
                        or changeType == "RETURN_TYPE_INSERT" \
                        or changeType == "RETURN_TYPE_DELETE":
                    if rootName in methodDb:  # 名字在Db里
                        methodDb.get(rootName)[3] += 1
                    else:  # 名字不在Db
                        methodDb[rootName] = [0, 0, 0, 1, 0, 0]
                # parameter
                elif changeType == "PARAMETER_INSERT" \
                        or changeType == "PARAMETER_RENAMING" \
                        or changeType == "PARAMETER_DELETE" \
                        or changeType == "PARAMETER_TYPE_CHANGE" \
                        or changeType == "PARAMETER_ORDERING_CHANGE":
                    if rootName in methodDb:  # 名字在Db里
                        methodDb.get(rootName)[4] += 1
                    else:  # 名字不在Db
                        methodDb[rootName] = [0, 0, 0, 0, 1, 0]
                # prefix
                elif changeType == "INCREASING_ACCESSIBILITY_CHANGE" \
                        or changeType == "REMOVING_METHOD_OVERRIDABILITY" \
                        or changeType == "ADDING_METHOD_OVERRIDABILITY" \
                        or changeType == "DECREASING_ACCESSIBILITY_CHANGE" \
                        or changeType == "UNCLASSIFIED_CHANGE":
                    if rootName in methodDb:  # 名字在Db里
                        methodDb.get(rootName)[5] += 1
                    else:  # 名字不在Db
                        methodDb[rootName] = [0, 0, 0, 0, 0, 1]
                else:
                    if rootName not in methodDb:  # 名字不在在Db里
                        methodDb[rootName] = [0, 0, 0, 0, 0, 0]
    return methodDb


# 从parser导出halstead数据，halstead_vocabulary，halstead_length，halstead_difficulty，halstead_volume，halstead_effort，halstead_bugs
def getHalsteadMetric(method_metric_path):
    halsteadMetric = {}
    with open(method_metric_path, "r", encoding='utf-8')as f:
        reader = csv.reader((line.replace('\0', '') for line in f))
        for metric in reader:
            if metric[0] == "id":
                continue
            methodName = metric[1]
            if (methodName.__contains__("/java/org")):
                methodName = replaceAll(methodName[(methodName.find('/java/org') + 6):(methodName.rfind('('))], '/',
                                        '.')
                halsteadMetric[methodName] = metric[8:14]
    return halsteadMetric


# 将结果写入csv文件
def writeResult(metric_kv, method_metric_kv, history_modify, halstead_metric, write_path):
    with open(write_path, "w", encoding='utf-8', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(
            ["Method name", "CountInput", "CountLine", "CountLineBlank", "CountLineCode", "CountLineCodeDecl",
             "CountLineCodeExe", "CountLineComment", "CountOutput", "CountPath", "CountStmt", "CountStmtDecl",
             "CountStmtExe", "Cyclomatic", "MaxNesting", "ParaCount",
             "halstead_vocabulary", "halstead_length", "halstead_difficulty", "halstead_volume", "halstead_effort",
             "halstead_bugs",
             "statementModify", "expressionModify", "commentModify", "returnTypeModify", "parameterModify",
             "prefixModify",
             "statementModify/countStmt", "expressionModify/Total LOC", "commentModify/Total LOC",
             "parameterModify/ParaCount",
             "Churn count", "Churned num", "DeleteNum", "Churned num/Total LOC", "Deleted num/Total LOC",
             "Churned num/Deleted num", "Nums worked on/Churn count",
             "authorNum", "bug-prone"
             ])
        for method_name in metric_kv:

            if not method_metric_kv.__contains__(method_name):
                continue
            if not history_modify.__contains__(method_name):
                continue
            if not halstead_metric.__contains__(method_name):
                continue
            total_loc = metric_kv[method_name][1]
            para_count = metric_kv[method_name][0]
            statement_count = method_metric_kv[method_name][9]
            historyPerLoc = []
            historyList = history_modify[method_name]
            if statement_count != "0":
                historyPerLoc.append(historyList[0] / int(statement_count))
            else:
                historyPerLoc.append(0)
            if total_loc != "0":
                historyPerLoc.append(historyList[1] / int(total_loc))
            else:
                historyPerLoc.append(0)
            if total_loc != "0":
                historyPerLoc.append(historyList[2] / int(total_loc))
            else:
                historyPerLoc.append(0)
            if para_count != "0" and para_count != 0:
                historyPerLoc.append(historyList[4] / int(para_count))
            else:
                historyPerLoc.append(0)
            writer.writerow([method_name] +
                            method_metric_kv[method_name] +
                            [para_count] +
                            halstead_metric[method_name] +
                            history_modify[method_name] +
                            historyPerLoc +
                            metric_kv[method_name][2:])

# 主函数
def main(xml_path, log_path, method_change_path, understand_output_path, method_metric_path, write_path):

    bug_id_set = getBugIdSet(xml_path)

    commit_bug_kv = getCommitBugKV(log_path, getBugName(bug_id_set))

    method_db = getMethodDb(method_change_path)

    metric_kv = getChurn(method_metric_path, method_db, bug_id_set, commit_bug_kv)

    method_metric_kv = getMethodMetric(understand_output_path)

    history_modify = getMetricHistory(method_change_path)

    halstead_metric = getHalsteadMetric(method_metric_path)

    writeResult(metric_kv, method_metric_kv, history_modify, halstead_metric, write_path)




if __name__ == '__main__':
    xml_path = 'C:\\Users\\wsz\\Desktop\\Teacher\\20.6.17\\metricChange\\activemq\\activemqSearchRequest.xml'
    log_path = 'C:\\Users\\wsz\\Desktop\\Group\\apache_project_codeAndMetric\\source_code\\activemq\\activemq_log.txt'
    method_change_path = 'C:\\Users\\wsz\\Desktop\\Teacher\\20.6.17\\metricChange\\activemq_methodChange.csv'
    understand_output_path = 'C:\\Users\\wsz\\Desktop\\Teacher\\20.8.15\\metric\\activemq.csv'
    method_metric_path = 'C:\\Users\\wsz\\Desktop\\Teacher\\20.8.15\\metric\\activemq_methodMetric.csv'
    write_path = 'C:\\Users\\wsz\\Desktop\\result.csv'

    if sys.argv.__len__() == 7:
        xml_path = sys.argv[1]
        log_path = sys.argv[2]
        method_change_path = sys.argv[3]
        understand_output_path = sys.argv[4]
        method_metric_path = sys.argv[5]
        write_path = sys.argv[6]
        sys.exit(main(xml_path, log_path, method_change_path, understand_output_path, method_metric_path, write_path))

    else:
        print("fail")
        raise RuntimeError('error args!')

