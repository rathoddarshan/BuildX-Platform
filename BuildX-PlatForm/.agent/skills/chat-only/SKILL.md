---
name: chat-only-mode
description: Use this skill when the user wants code solutions, explanations, or snippets outputted directly in the chat interface without editing any workspace files.
---

# Chat Only Mode

This skill instructs the agent to act as a conversational advisor rather than an autonomous code editor.

## Use this skill when
- The user asks for a solution, fix, or explanation but specifies they want to see it in the chat or terminal.
- The user explicitly requests that no files in the codebase be modified or edited.

## Do not use this skill when
- The user explicitly asks the agent to write, create, or update files in the codebase directly.

## Instructions
1. **DO NOT** call any file-editing or command-execution tools (such as `write_to_file`, `replace_file_content`, or `run_command`) that modify the codebase.
2. Present the complete solution, code modifications, or new files directly in the chat window using markdown code blocks.
3. Clearly explain where each code snippet belongs (e.g., file path, class name, line numbers) so the user can manually copy and paste it if desired.
