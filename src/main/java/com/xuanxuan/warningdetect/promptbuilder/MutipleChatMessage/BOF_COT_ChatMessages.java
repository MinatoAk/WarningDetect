package com.xuanxuan.warningdetect.promptbuilder.MutipleChatMessage;

public interface BOF_COT_ChatMessages {
    String BOF_COT_Q1 =
            "# Bug Report\n" +
            "```json\n" +
            "{\n" +
            "    \"bug_type\": \"ctuArrayIndex\",\n" +
            "    \"line\": 60,\n" +
            "    \"file\": \"char_alloca_loop_04.c\",\n" +
            "    \"qualifier\": \"Array index out of bounds; 'data' buffer size is 10 and it is accessed at offset 10.\",\n" +
            "    \"Trace\": {\"filename\": \"drivers/pn532/pn532.c\", \"line_number\": 49, \"column_number\": 8, \"description\": \"\"},\n" +
            "       \n" +
            "}\n" +
            "```\n" +
            "\n" +
            "# Code Snippet\n" +
            "```C,C++\n" +
            "static const int STATIC_CONST_TRUE = 1; /* true */\n" +
            "static const int STATIC_CONST_FALSE = 0; /* false */\n" +
            "\n" +
            " static void goodG2B1()\n" +
            "{\n" +
            "    char * data;\n" +
            "    char * dataBadBuffer = (char *)ALLOCA((10)*sizeof(char));\n" +
            "    char * dataGoodBuffer = (char *)ALLOCA((10+1)*sizeof(char));\n" +
            "    if(STATIC_CONST_FALSE)\n" +
            "    {\n" +
            "        /* INCIDENTAL: CWE 561 Dead Code, the code below will never run */\n" +
            "        printLine(\"Benign, fixed string\");\n" +
            "    }\n" +
            "     else\n" +
            "    {\n" +
            "        /* FIX: Set a pointer to a buffer that leaves room for a NULL terminator when performing\n" +
            "         * string copies in the sinks  */\n" +
            "        data = dataGoodBuffer;\n" +
            "     data[0] = '\\0'; /* null terminate */\n" +
            "    }\n" +
            "    {\n" +
            "        char source[10+1] = SRC_STRING;\n" +
            "        size_t i, sourceLen;\n" +
            "       sourceLen = strlen(source);\n" +
            "        /* Copy length + 1 to include NUL terminator from source */\n" +
            "       /* POTENTIAL FLAW: data may not have enough space to hold source */\n" +
            "        for (i = 0; i < sourceLen + 1; i++)\n" +
            "        {\n" +
            "            data[i] = source[i];\n" +
            "        }\n" +
            "        printLine(data);\n" +
            "    }\n" +
            "}\n" +
            "```";

    String BOF_COT_A1 =
            "According to the bug report, the bug is of type ctuArrayIndex and occurs at line 60 in the file char_alloca_loop_04.c.\n" +
            "The qualifier from Cppcheck states that the 'data' buffer size is 10 and it is accessed at offset 10, which indicates an array index out of bounds error.\n" +
            "Looking at the provided code snippet, we can see that the data pointer is assigned to dataGoodBuffer at line 49, which is allocated with a size of (10+1)*sizeof(char) at line 39.\n" +
            "This means that the data buffer has a size of 11. The for loop at lines 58-61 copies characters from the source array to the data array, with the loop condition being i < sourceLen + 1.\n" +
            "Since sourceLen is obtained by calling strlen(source), it does not include the null terminator.\n" +
            "Therefore, the loop will copy sourceLen + 1 characters from source to data, including the null terminator.\n" +
            "\n" +
            "In this case, it appears that the bug report is a false alarm, as the data buffer has enough space to hold the copied characters from source, including the null terminator.\n" +
            "\n" +
            "@@@ false alarm @@@";

    String BOF_COT_Q2 =
            "# Bug Report\n" +
            "```json\n" +
            "{\n" +
            "    \"bug_type\": \"Buffer Overrun L2\",\n" +
            "    \"line\": 148,\n" +
            "    \"file\": \"sys/hashes/sha3.c\",\n" +
            "    \"qualifier\": \"Offset: [-1, 575] Size: 200 by call to `Keccak`.\",\n" +
            "    \"Trace\":{\"filename\": \"sys/hashes/sha3.c\", \"line_number\": 148, \"column_number\": 5, \"description\": \"Call\"},\n" +
            "  \n" +
            "}\n" +
            "```\n" +
            "\n" +
            "# Code Snippet\n" +
            "```C,C++\n" +
            "\n" +
            "static void FIPS202_SHA3_512(const unsigned char *input, unsigned int inputByteLen, unsigned char *output)\n" +
            "{\n" +
            "     Keccak(576, 1024, input, inputByteLen, 0x06, output, 64);\n" +
            " }\n" +
            "```";

    String BOF_COT_A2 =
            "Based on the bug report and the code snippet, the bug report claims that there is a buffer overrun in line 148 of the file \"sys/hashes/sha3.c\" in the function \"FIPS202_SHA3_512\".\n" +
            "The bug report provides an error trace that indicates a call at line 148 followed by an array access at line 377 with an offset of [-1, 575] and a size of 200, caused by a call to the function `Keccak`.\n" +
            "\n" +
            "It takes several parameters, including `inputByteLen` and `outputByteLen`, which are both of type `unsigned long long int`.\n" +
            "In the code, the `inputByteLen` is used to determine the number of input blocks to process, and the `outputByteLen` is used to determine the number of output blocks to squeeze out.\n" +
            "\n" +
            "The error trace mentions an array access at line 377, which corresponds to the line `state[rateInBytes - 1] ^= 0x80;` in the `Keccak` function.\n" +
            "This line accesses the `state` array with an index of `rateInBytes - 1`.\n" +
            "However, there is no evidence in the error trace or the code snippet that suggests a buffer overrun at this location.\n" +
            "\n" +
            "Therefore, based on the information provided, I conclude that this is a false alarm.\n" +
            "\n" +
            "@@@ false alarm @@@";
}
