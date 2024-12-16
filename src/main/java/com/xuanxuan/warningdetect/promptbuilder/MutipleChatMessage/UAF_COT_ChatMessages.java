package com.xuanxuan.warningdetect.promptbuilder.MutipleChatMessage;

public interface UAF_COT_ChatMessages {
    String UAF_COT_Q1 =
            "# Bug Report\n" +
            "```json\n" +
            "{\n" +
            "    \"bug_type\": \"Use-after-free\",\n" +
            "    \"line\": \"2146\",\n" +
            "    \"file\": \"debug.c\",\n" +
            "    \"qualifier\": \"Use of memory after it is freed\",\n" +
            "    \"Trace_Nnode\":{\"filename\": \"debug.c\", \"line_number\": \"4833\", \"column_number\": \"1\", \"description\": \"Entered call from 'delete_breakpoint'\"},\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "# Code Snippet\n" +
            "```C,C++\n" +
            "4834 delete_commands_item(struct commands_item *c)\n" +
            "4835 {\n" +
            "4836 \tefree(c->cmd_string);\n" +
            "4837 \tfree_cmdarg(c->arg);\n" +
            "4838 \tc->next->prev = c->prev;\n" +
            "4839 \tc->prev->next = c->next;\n" +
            "4840 \tefree(c);\n" +
            "4841 }\n" +
            "```";

    String UAF_COT_A1 =
            "The bug report claims that there is a Use-After-Free bug in `delete_breakpoint` at line 2146. The variable used at line 2146 is `c->prev`.\n" +
            "To confirm if it is a real bug, I should analyze the code snippet based on the error trace in the bug report.\n" +
            "\n" +
            "According to the error trace, we should analyze `delete_breakpoint` first. We can see that `c` is declared at line 2133 as a `commands_item` pointer.\n" +
            "Then the program executes the loop at line 2145. This involves changes in the link list. I should analze it carefully.\n" +
            "I use node1, node2, and node3 to represent `c->prev`, `c` and `c->next` respectively.\n" +
            "At line 2146, `c` is set to `c->prev`. `c->prev` means the previous node of `c`. So `c` is node1 now.\n" +
            "Then at line 2147, `delete_commands_item` is called with `c->next`.\n" +
            "With this calling context, the `c` in `delete_commands_item` is the same as `c->next` in `delete_breakpoint`,\n" +
            "which means that the `c` in `delete_commands_item` and the `c->next` in `delete_breakpoint` is all node2. So the `c` in `delete_commands_item` is node1.\n" +
            "Then at line 4838, `c->next->prev` is set to `c->prev`, which means that the previous node of `c->next`(node3) is set to the previous node of `c` which is node1.\n" +
            "Then at line 4839, `c->prev->next` is set to `c->next`, which means that the next node of `c->prev`(node1) is the next node of `c` which is node3.\n" +
            "After that, node2 is remove from the link list and then is freed at line 4838.\n" +
            "The changes in `delete_commands_item` will be synchronized to `delete_breakpoint`, so the next node of `c`(node1) in `delete_breakpoint` is node3.\n" +
            "The node3 is not freed. \n" +
            "\n" +
            "Therefore, there is no use after free bug in code basd on the analysis above. The bug report is a false alarm.\n" +
            "\n" +
            "@@@ false alarm @@@";

    String UAF_COT_Q2 =
            "# Bug Report\n" +
            "```json\n" +
            "{\n" +
            "    \"bug_type\": \"Use-after-free\",\n" +
            "    \"line\": \"48\",\n" +
            "    \"file\": \"aaa_15.c\",\n" +
            "    \"qualifier\":\"Use of memory after it is freed\",\n" +
            "    \"Trace_None\": {\"filename\": \"aaa_15.c\", \"line_number\": \"32\", \"column_number\": \"24\", \"description\": \"Memory is allocated\"},\n" +
            "\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "# Code Snippet\n" +
            "```C,C++\n" +
            "void aaa_15_bad()\n" +
            "{\n" +
            "    char * data;\n" +
            "    /* Initialize data */\n" +
            "   data = NULL;\n" +
            "   switch(6)\n" +
            "    {\n" +
            "    case 6:\n" +
            "        data = (char *)malloc(100*sizeof(char));\n" +
            "        if (data == NULL) {exit(-1);}\n" +
            "        memset(data, 'A', 100-1);\n" +
            "        data[100-1] = '\\0';\n" +
            "        /* POTENTIAL FLAW: Free data in the source - the bad sink attempts to use data */\n" +
            "        free(data);\n" +
            "        break;\n" +
            "    default:\n" +
            "        /* INCIDENTAL: CWE 561 Dead Code, the code below will never run */\n" +
            "        printLine(\"Benign, fixed string\");\n" +
            "        break;\n" +
            "    }\n" +
            "    switch(7)\n" +
            "    {\n" +
            "    case 7:\n" +
            "        /* POTENTIAL FLAW: Use of data that may have been freed */\n" +
            "        printLine(data);\n" +
            "        /* POTENTIAL INCIDENTAL - Possible memory leak here if data was not freed */\n" +
            "        break;\n" +
            "    default:\n" +
            "        /* INCIDENTAL: CWE 561 Dead Code, the code below will never run */\n" +
            "        printLine(\"Benign, fixed string\");\n" +
            "        break;\n" +
            "    }\n" +
            "}\n" +
            "```";

    String UAF_COT_A2 =
            "The bug report claims that there is a Use-after-free bug in `aaa_15_bad` at line 48.\n" +
            "To confirm if it is a real bug, I should analyze the code snippet based on the error trace in the bug report.\n" +
            "The variable used at line 48 is `data`, so I should analyze if `data` is used at line 48 after it is freed.\n" +
            "\n" +
            "According to the error trace, `data` is declared at line 28 in the code snippet.\n" +
            "Then the program will execute the first `switch` block. The condition is `6`, so the block of `case 6` is executed.\n" +
            "In the `case 6` block, `data` is allocated memory with `malloca` at line 32 and then freed at line 37.\n" +
            "Then the program will execute the second `switch` block. The condition is `7`, so the block of `case 7` is executed.\n" +
            "In the `case 6` block, `data` is used as the parameter of `printLine`.\n" +
            "However, `data` is already freed at line 37. Therefore, `data` is indeed used after free at line 48.\n" +
            "\n" +
            "Basd on the analysis above, I can conclude that there is indeed a Use-after-free bug in `aaa_15_bad` at line 48.\n" +
            "\n" +
            "@@@ real bug @@@";
}
