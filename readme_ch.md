# FCHE

FCHE (Fine-grained Code Metrics and History Measures Extractor ) 一个用于解析方法级别代码度量和历史信息的工具。


请查看论文获取更详细的信息: An exploratory study of bug prediction at the method level(Information and Software Technology 144 (2022) 106794
Available online 7 December 2021 0950-5849/© 2021 Elsevier B.V. All rights reserved.)

# Metrics

## Method Metric

| Method Metric                   | Description                                                  |
| :------------------------------ | ------------------------------------------------------------ |
| Lines of Code                   | 代码行数 (LOC)                |
| Number of Comment lines         | 评论行数                          |
| Number of all lines             | 所有行数                             |
| Number of Blank lines           | 空行数                      |
| Number of Declare lines         | 声明语句行数         |
| Number of Executable lines      | 可执行语句行数         |
| Number of Parameters            | 参数的个数                          |
| Number of Statements            | 语句块的个数                         |
| Number of Declare Statements    | 声明语句块个数             |
| Number of Executable Statements | 可执行语句块个数                |
| Halstead-Vocabulary             | 操作符种类(η1) 和 操作数种类(η2)的个数和.(η = η1+η2) |
| Halstead-Length                 | 操作符总数(N1) 和 操作数总数(N2)的和.(N = N1+N2)   |
| Halstead-Difficulty             | D = η1/2 * N2/η2                                             |
| Halstead-Volume                 | V = N * log2(η)                                              |
| Halstead-Effort                 | effort = D * V                                               |
| Halstead-Bugs                   | bugs = E^(2/3) / 3000                                        |
| Cyclomatic complexity           | 方法的圈复杂的                           |
| Number of Path                  | 可能的路径数                                    |
| Max Nesting                     | 最大嵌套层级数                |
| Fan-in                          | 扇入    |
| Fan -out                        | 扇出     |

## History Metric

| History Metric                                        | Description                                                  |
| ----------------------------------------------------- | ------------------------------------------------------------ |
| Added LOC                                             | 历史修改添加行数     |
| Deleted LOC                                           | 历史修改扇出行数    |
| All Changed LOC                                       | 增加和修改的行数和                           |
| Number of Changes                                     | bug导致的历史提交总数                        |
| Number of Authors                                     | 参与修改的成员数 |
| Number of Modified Statements                         | 语句的增加、删除、修改、顺序改变数之和  |
| Number of Modified Expressions                        | if while 等控制语句的增、删 |
| Number of Modified Comments                           | 评论语句的增、删、改、移动 |
| Number of Modified Return type                        | 返回类型的增、删、改 |
| Number of Modified Parameters                         | 参数的增、删、重命名、类型修改、顺序修改  |
| Number of Modified Prefix                             | 方法名前的访问修饰等的修改（例如 final, static, public） |
| Added LOC / LOC                                       | Added LOC 和 LOC 之比                             |
| Deleted LOC / LOC                                     | Deleted LOC 和 LOC 之比                          |
| Added LOC / Deleted LOC                               | Added LOC 和 Deleted LOC 之比                   |
| Changed LOC / Number of Changes                       | Changed LOC 和 Number of Changes 之比            |
| Number of Modified Statements / Number of Statements  | Number of Modified Statements 和 Number of Statements 之比 |
| Number of Modified Expressions / Number of Statements | Number of Modified Expressions 和 Number of Statements 之比 |
| Number of Modified Comments/ LOC                      | Number of Modified Comments 和 LOC 之比            |
| Number of Modified Parameters/ Number of Parameters   | Number of Modified Parameters 和 Number of Parameters 之比 |



# Features

FCHE 目前支持java语言的分析


# Usage

###  1) Set up Java environment and Python environment
需要java1.8的环境支持(jdk1.8)
需要python3.0的环境支持.

###  2)Use 
编译运行前需要设置的项目参数:
< language > < dir > < project-name > < xml_path >  < log_path > < method_change_path > < understand_output_path > <print_mod>


- < **language** >. 目前支持java
- < **dir** >. 需要分析的原文件的路径
- < **project-name** >. 简短的项目别名  
- < **xml_path** >. 通过版本管理平台 [JIRA](https://www.atlassian.com/software/jira)导出的XML文件（内容格式与example保持一致）
- < **log_path** >. 通过 [git](https://git-scm.com/)导出的txt文件（内容格式需与example保持一致）
- < **method_change_path** >. 通过 [changedistiller](https://github.com/sealuzh/tools-changedistiller) 导出的csv文件（格式与example保持一致）
- < **understand_output_path** >. 通过 [understand](https://scitools.com/) 导出的文件（内容格式与example保持一致）
- < **print_mod**>. 两种可选导出模式（模式1 : 结果显示 bug-prone 或者 not bug-prone;
 模式2 : 结果显示具体bug数(0,1,2...)

请确保**格式**以及**数据的顺序**与example中保持一致，否则结果可能不准确。

#### Example 
在运行程序前, 你需要设置项目的参数（以Idea为例需要进入：Run-Edit Configurations-Program arguments，然后输入以下8个参数）:

java example/avro avro input/avroSearchRequest.xml input/avro_log.txt input/avro_methodChange.csv input/avro_understand.csv 1


然后编译运行项目，就可在项目中得到结果 avro-out/avro_result.csv

# paper
论文中涉及的相关数据、以及过程的介绍

## data/paper
Cal80Bug.java 
这个文件可用于计算前80%的bug方法数（对应论文 discuss 4.6 的 Figure2 相关数据）
CalBugNum.java
这个文件用于计算bug-prone方法数、行数，not bug-prone方法数、行数；以及行数处于各个范围(1-10 , 11-25, 26-50, 50+ )的方法个数。（对应discuss 4.5 的 Table 16 和 discuss 4.7 的 Table 17）

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