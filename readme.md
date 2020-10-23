# FCHE

FCHE (Fine-grained Code Metrics and History Measures Extractor ) is a tool for extraction of fine-graind code metrics:
paraCount,lineCount,simpleCc,maxBlockDepth,execStmt,localVarDecl,
[Halstead Metrics](https://en.wikipedia.org/wiki/Halstead_complexity_measures).
It can also integrate some other data obtained through other tools, including data obtained from 
[understand](https://scitools.com/) or [changedistiller](https://github.com/sealuzh/tools-changedistiller).

# Features
FCHE supports analyzing source code written in Java

# Usage
###  1) Set up Java environment and Python environment
you should set up Java environment.(jdk1.8)
you should set up Python3.0 environment.

###  2)Use 
before compile the source code,you should set Program arguments:
java < dir > < project-name > < xml_path >  < log_path > < method_change_path > < understand_output_path >
- < dir >. The path of the source code that will be analyzed.
- < project-name >. A short alias name of the analyzed source code project.  
- <xml_path>. A XML file that can be get on many version management platform like [JIRA](https://www.atlassian.com/software/jira).
- <log_path>. A text file that can be get by [git](https://git-scm.com/). 
- <method_change_path>. A CSV file that can be get by [changedistiller](https://github.com/sealuzh/tools-changedistiller).
- <understand_output_path>. A CSV file that can be get by [understand](https://scitools.com/).
Please be sure to use files in the same format as the example, and ensure that they are in the same order, otherwise the results may be inaccurate

#### Example 
before run this project, you should Compile Configurations and set Program arguments:

java
example/avro
avro
input/avroSearchRequest.xml
input/avro_log.txt
input/avro_methodChange.csv
input/avro_understand.csv


then run the project,you will get activemq-out/activemq_result.csv.

