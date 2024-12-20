# T19 - PA - 基于提示工程的智能化源码警告识别实证研究

使用不同的提示词模板类型，调用大模型提供的 SDK，验证静态分析工具得到的程序警告的正确性；

<br>

## 1 Quick Start

为了方便运行，直接执行 `WarningDetectMain` 文件下的 `main` 方法即可，无需使用 SpringBoot 启动；

### 1.1 单轮对话

1) 在 `constant.APIKeys` 文件下添加自己的 `api key`，对应的获取方式在该文件下提供；

2) 修改希望运行的提示词模板和对应模板的日志路径；

```java
private static final String systemPrompt = <希望使用的提示词模板的系统预设>;
private static final String logFilePath = <对应模板的系统预设>;
```

3) 选择运行的数据集；

```java
String type = <"cpp" / "java">;
warningDetectMain.getWarningData(type);
```

4) 选择单轮对话调用方式

```java
String userPrompt = promptBuilder.buildUserPrompt(warningData);
String res = zhiPuAIManager.doStableSyncRequest(systemPrompt, userPrompt, warningDetectMain.clientV4);
```

### 1.2 Few - Shots

只有 `cpp` 数据集提供了 Few Shots 提示词模板，需要对 `cpp` 数据集进行额外处理；

1) 读取完整的 `cpp` 数据；

```java
warningDetectMain.getCPPJsonData();
```

2) 选择 Few Shots 调用方式及希望实用的提示词模板类型，注释单轮调用方式；

```java
List<ChatMessage> messages = 
    promptBuilder.buildUAFMutipleChatMessages(systemPrompt, cppWarningDataDTO);
String res = zhiPuAIManager.doMutipleChatRequest(messages, warningDetectMain.clientV4);
```

</br>

## 2 Docs

`logs` 文件夹下为所有的数据集运行记录:

- `error_data_index.log` 记录当前轮运行过程中调用超时的数据，后续可以重试回捞实现补偿；
- `test.log` 为测试日志，供开发人员使用；
- `LLMResult.log` 记录所有 {大模型，提示词模板，数据集} 对应的测试结果，包含结果为 TP, TN, FP, FN, NK 的数据的个数，以及 Accuracy, Precision, Recall, F1 的结果；
- `cpp, Java` 文件夹下记录了完整的大模型回答内容；

</br>

`datas` 文件夹下为使用的数据集，包含 `cpp` 数据 956 条，`Java` 数据 1090 条；