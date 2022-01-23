# FCHE

FCHE (Fine-grained Code Metrics and History Measures Extractor ) 一个用于解析方法级别代码度量和历史信息的工具。


详情请查看论文: An exploratory study of bug prediction at the method level(Information and Software Technology 144 (2022) 106794
Available online 7 December 2021 0950-5849/© 2021 Elsevier B.V. All rights reserved.)

# Metrics

## Method Metric

| Method Metric                   | Description                                                  |
| :------------------------------ | ------------------------------------------------------------ |
| Lines of Code                   | Number of lines containing source code. (LOC)                |
| Number of Comment lines         | Number of lines containing comment.                          |
| Number of all lines             | Number of all lines in a method.                             |
| Number of Blank lines           | Number of all blank lines in a method.                       |
| Number of Declare lines         | Number of lines containing declarative source code.          |
| Number of Executable lines      | Number of lines containing executable source code.           |
| Number of Parameters            | Number of parameters in a method.                            |
| Number of Statements            | Numbera of statements in a method.                           |
| Number of Declare Statements    | Number of declarative statements in a method.                |
| Number of Executable Statements | Number of executable statements in a method.                 |
| Halstead-Vocabulary             | Sum of **distinct** of operators(η1) and oprands(η2).(η = η1+η2) |
| Halstead-Length                 | Sum of number of operators(N1) and oprands(N2).(N = N1+N2)   |
| Halstead-Difficulty             | D = η1/2 * N2/η2                                             |
| Halstead-Volume                 | V = N * log2(η)                                              |
| Halstead-Effort                 | effort = D * V                                               |
| Halstead-Bugs                   | bugs = E^(2/3) / 3000                                        |
| Cyclomatic complexity           | Cyclomatic complexity of a method.                           |
| Number of Path                  | Number of possible paths.                                    |
| Max Nesting                     | Maximum nesting level of control constructs.                 |
| Fan-in                          | Number of calling subprograms plus global variables read.    |
| Fan -out                        | Number of called subprograms plus global variables set.      |

## History Metric

| History Metric                                        | Description                                                  |
| ----------------------------------------------------- | ------------------------------------------------------------ |
| Added LOC                                             | Total number of lines added in historical modification.      |
| Deleted LOC                                           | Total number of lines deleted in historical modification.    |
| All Changed LOC                                       | Sum of Added LOC and Deleted LOC.                            |
| Number of Changes                                     | Number of commits due to bug fixing.                         |
| Number of Authors                                     | Number of authors who participated in modifying the bug fixing of a method. |
| Number of Modified Statements                         | The total number of statement additions, deletions, modifications, and sequence changes. |
| Number of Modified Expressions                        | Number of conditional change, deletion, insertion in if, while, etc. expressions. |
| Number of Modified Comments                           | Total number of comments added, deleted, modified, and moved. |
| Number of Modified Return type                        | Total number of additions, deletions, and changes of Return type. |
| Number of Modified Parameters                         | Total number of parameter additions, deletions, renamings, type changes, and order changes. |
| Number of Modified Prefix                             | Total number of changes of prefix information such as final, static, public. |
| Added LOC / LOC                                       | The ratio of Added LOC and LOC.                              |
| Deleted LOC / LOC                                     | The ratio of Deleted LOC and LOC.                            |
| Added LOC / Deleted LOC                               | The ratio of Added LOC and Deleted LOC.                      |
| Changed LOC / Number of Changes                       | The ratio of Changed LOC and Number of Changes.              |
| Number of Modified Statements / Number of Statements  | The ratio of Number of Modified Statements and Number of Statements. |
| Number of Modified Expressions / Number of Statements | The ratio of Number of Modified Expressions and Number of Statements. |
| Number of Modified Comments/ LOC                      | The ratio of Number of Modified Comments and LOC.            |
| Number of Modified Parameters/ Number of Parameters   | The ratio of Number of Modified Parameters and Number of Parameters. |



# Features

FCHE 目前仅支持java代码的分析


# Usage
###  1) Set up Java environment and Python environment

需要java1.8的环境支持(jdk1.8)
需要python3.0的环境支持.

###  2)Use 

运行前需要准备以下各种文件：
并确保**格式**以及**数据的顺序**与example中保持一致，否则结果可能不准确。
- < **language** >. 目前支持java
- < **dir** >. 需要分析的原文件的路径
- < **project-name** >. 简短的项目别名  
- < **xml_path** >. 通过版本管理平台 [JIRA](https://www.atlassian.com/software/jira)导出的XML文件（内容格式与example保持一致）
- < **log_path** >. 通过 [git](https://git-scm.com/)导出的txt文件（内容格式需与example保持一致）
- < **method_change_path** >. 通过 [changedistiller](https://github.com/sealuzh/tools-changedistiller) 导出的csv文件（格式与example保持一致）
- < **understand_output_path** >. 通过 [understand](https://scitools.com/) 导出的文件（内容格式与example保持一致）
- < **print_mod**>. 两种可选导出模式（模式1 : 结果显示 bug-prone 或者 not bug-prone;
 模式2 : 结果显示具体bug数(0,1,2...)

然后通过command进入out/artifacts/FCHE_jar/目录下
#### Example 

进入FCHE_jar目录 使用命令：
java -jar FCHE.jar java example/avro avro input/avroSearchRequest.xml input/avro_log.txt input/avro_methodChange.csv input/avro_understand.csv 1
可获得bug-prone结果为bug-prone或not bug-prone类型的result

java -jar FCHE.jar java example/avro avro input/avroSearchRequest.xml input/avro_log.txt input/avro_methodChange.csv input/avro_understand.csv 2
可获得bug-prone结果为1,2,3...类型的result

# paper

论文中涉及的相关数据、以及过程的介绍

## data/paper

Cal80Bug.jar
这个jar文件可用于计算前80%的bug方法数（对应论文中 discuss 4.6 的 Figure2 相关数据）
cd进入paper目录，使用命令 java -jar Cal80Bug.jar ../avro-out/avro_result2.csv

CalBugNum.jar
这个jar文件可用于计算bug-prone方法数、行数，not bug-prone方法数、行数；以及行数处于各个范围(1-10 , 11-25, 26-50, 50+ )的方法个数。（对应论文中discuss 4.5 的 Table 16 和 discuss 4.7 的 Table 17）
cd进入paper目录，使用命令 java -jar Cal80Bug.jar ../avro-out/avro_result1.csv

## source_code/source_code.txt

源代码的下载路径

## bug_repository.zip

从 [JIRA](https://www.atlassian.com/software/jira)平台下载的xml数据

## change_metric.zip

通过 [changedistiller](https://github.com/sealuzh/tools-changedistiller)解析的数据

## revision_history.zip

通过 [git](https://git-scm.com/)获得的数据

使用 "git log --numstat" 命令得到

## understand_output.zip

通过 [understand](https://scitools.com/) 解析获得的数据