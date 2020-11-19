# FCHE

FCHE (Fine-grained Code Metrics and History Measures Extractor ) is a tool for extraction of fine-grained code metrics and history measures,
like Number of Parameters, Lines of Code or [Halstead-Metrics](https://en.wikipedia.org/wiki/Halstead_complexity_measures) etc.
Also, It can integrate some other data obtained through other tools, including data obtained from [understand](https://scitools.com/) or [changedistiller](https://github.com/sealuzh/tools-changedistiller).

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

FCHE supports analyzing source code written in Java



# Usage

###  1) Set up Java environment and Python environment
you should set up Java environment.(jdk1.8)
you should set up Python3.0 environment.

###  2)Use 
before compile the source code,you should set Program arguments:
< language > < dir > < project-name > < xml_path >  < log_path > < method_change_path > < understand_output_path >


- < **language** >. Only support Java now.
- < **dir** >. The path of the source code that will be analyzed.
- < **project-name** >. A short alias name of the analyzed source code project.  
- < **xml_path** >. A XML file that can be get on many version management platform like [JIRA](https://www.atlassian.com/software/jira).
- < **log_path** >. A text file that can be get by [git](https://git-scm.com/). 
- < **method_change_path** >. A CSV file that can be get by [changedistiller](https://github.com/sealuzh/tools-changedistiller).
- < **understand_output_path** >. A CSV file that can be get by [understand](https://scitools.com/).
Please **be sure** to use files in the **same** **format** as the **example**, and ensure that they are in the **same order**, otherwise the results may be inaccurate.

#### Example 
before run this project, you should **Compile Configurations** and set Program arguments:

java example/avro avro input/avroSearchRequest.xml input/avro_log.txt input/avro_methodChange.csv input/avro_understand.csv


then run the project, you will get activemq-out/activemq_result.csv in project.

