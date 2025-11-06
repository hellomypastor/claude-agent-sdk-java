# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0] - 2024-11-06

### Added
- Initial Java implementation of Claude Agent SDK
- Core type system (Messages, ContentBlocks, etc.)
- ClaudeSDKClient for bidirectional communication
- Simple query API for one-shot interactions
- MCP (Model Context Protocol) support with in-process server capability
- Custom tool definition system
- Hook system for policy enforcement
- Permission management system
- Configuration options builder
- Error handling with specific exception types
- Process-based transport layer
- Example code for common use cases
- Comprehensive documentation

### Features
- Query API compatible with Python SDK
- Bidirectional client for stateful conversations
- Custom tools as Java methods
- Hook callbacks for agent loop control
- Mixed MCP server support (in-process and external)
- Flexible permission modes
- Working directory configuration
- System prompt customization

### Notes
- This is a Java port of the Python SDK version 0.1.6
- Requires Java 11 or higher
- Requires Claude Code CLI 2.0.0+
- Maintains API compatibility with Python SDK where applicable
