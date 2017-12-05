# Contributing to Squanchy
>Huge props to the Atom contribution guidelines for inspiring these

The following is a set of guidelines for contributing to Squanchy and its packages, which are hosted in the [Squanchy Organization](https://github.com/squanchy-dev) on GitHub. These are mostly guidelines, not rules. Use your best judgment, and feel free to propose changes to this document in a pull request.

#### Table Of Contents

[Code of Conduct](#code-of-conduct)

[I don't want to read this whole thing, I just have a question!!!](#i-dont-want-to-read-this-whole-thing-i-just-have-a-question)

[What should I know before I get started?](#what-should-i-know-before-i-get-started)

[How Can I Contribute?](#how-can-i-contribute)
  * [Reporting Bugs](#reporting-bugs)
  * [Suggesting Enhancements](#suggesting-enhancements)
  * [Your First Code Contribution](#your-first-code-contribution)
  * [Pull Requests](#pull-requests)

[Styleguides](#styleguides)
  * [Git Commit Messages](#git-commit-messages)

[Additional Notes](#additional-notes)
  * [Issue and Pull Request Labels](#issue-and-pull-request-labels)

## Code of Conduct

This project and everyone participating in it is governed by the [Squanchy Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. Please report unacceptable behavior to [squanchy-dev@gmail.com](squanchy-dev@gmail.com).

## I don't want to read this whole thing I just have a question!!!

> **Note:** Please don't file an issue to ask a question. You'll get faster results by using the resources below.

If you want to ask a question, get in touch with us [on Twitter](https://twitter.com/seebrock3r) or drop us a line to [squanchy-dev@gmail.com](squanchy-dev@gmail.com).

## What should I know before I get started?

Squanchy is an open source project made of three main repositories. When you initially consider contributing to Squanchy, you might be unsure about which of those two repositories implements the functionality you want to change or report a bug for. This section should help you with that.

 * [`squanchy-android`](https://github.com/squanchy-dev/squanchy-android) is the native Android app
 * [`squanchy-flutter`](https://github.com/squanchy-dev/squanchy-flutter) is the iOS app, developed in Flutter
 * [`squanchy-firebase`](https://github.com/squanchy-dev/squanchy-firebase) is the Firebase backend code

## How Can I Contribute?

### Reporting Bugs

This section guides you through submitting a bug report for Squanchy. Following these guidelines helps maintainers and the community understand your report :pencil:, reproduce the behavior :computer: :computer:, and find related reports :mag_right:.

Before creating bug reports, please check [this list](#before-submitting-a-bug-report) as you might find out that you don't need to create one. When you are creating a bug report, please [include as many details as possible](#how-do-i-submit-a-good-bug-report). Fill out [the required template](ISSUE_TEMPLATE.md), the information it asks for helps us resolve issues faster.

> **Note:** If you find a **Closed** issue that seems like it is the same thing that you're experiencing, open a new issue and include a link to the original issue in the body of your new one.

#### Before Submitting A Bug Report

* **Checkout the repository and try debugging the app.** You might be able to find the cause of the problem and fix things yourself. Most importantly, check if you can reproduce the problem in the latest version of Squanchy (see the releases for the relevant repository), and if you can get the desired behavior by changing the code yourself.
* **Perform a [cursory search](https://github.com/search?q=+is%3Aissue+user%3Asquanchy-dev)** to see if the problem has already been reported. If it has **and the issue is still open**, add a comment to the existing issue instead of opening a new one. Please avoid "me too" comments on issues.

#### How Do I Submit A (Good) Bug Report?

Bugs are tracked as [GitHub issues](https://guides.github.com/features/issues/). After you've determined which repository your bug is related to, create an issue on that repository and provide the following information by filling in [the template](ISSUE_TEMPLATE.md).

Explain the problem and include additional details to help maintainers reproduce the problem:

* **Use a clear and descriptive title** for the issue to identify the problem
* **Describe the exact steps which reproduce the problem** in as many details as possible. For example, start by explaining how you started Squanchy, e.g., which command exactly you used in the terminal, or how you started Squanchy otherwise. When listing steps, **don't just say what you did, but explain how you did it**. For example, if you moved the cursor to the end of a line, explain if you used the mouse, or a keyboard shortcut or an Squanchy command, and if so which one?
* **Provide specific examples to demonstrate the steps**. Include links to files or GitHub projects, or copy/pasteable snippets, which you use in those examples. If you're providing snippets in the issue, use [Markdown code blocks](https://help.github.com/articles/markdown-basics/#multiple-lines)
* **Describe the behavior you observed after following the steps** and point out what exactly is the problem with that behavior
* **Explain which behavior you expected to see instead and why**
* **Include screenshots and animated GIFs** which show you following the described steps and clearly demonstrate the problem
* **If you're reporting that Squanchy crashed**, include a crash report with a stack trace from the operating system if possible. Include the crash report in the issue in a [code block](https://help.github.com/articles/markdown-basics/#multiple-lines), a [file attachment](https://help.github.com/articles/file-attachments-on-issues-and-pull-requests/), or put it in a [gist](https://gist.github.com/) and provide link to that gist
* **If the problem wasn't triggered by a specific action**, describe what you were doing before the problem happened and share more information using the guidelines below

Provide more context by answering these questions:

* **Did the problem start happening recently** (e.g., after updating to a new version of Squanchy) or was this always a problem?
* If the problem started happening recently, **can you reproduce the problem in an older version of Squanchy?** What's the most recent version in which the problem doesn't happen? You can download older versions of Squanchy from the repository's releases page
* **Can you reliably reproduce the issue?** If not, provide details about how often the problem happens and under which conditions it normally happens

Include details about your configuration and environment:

* **Which version of Squanchy are you using?** You can get the exact version by checking the Squanchy Settings screen
* **What's the name and version of the OS you're using**?

### Suggesting Enhancements

This section guides you through submitting an enhancement suggestion for Squanchy, including completely new features and minor improvements to existing functionality. Following these guidelines helps maintainers and the community understand your suggestion :pencil: and find related suggestions :mag_right:.

Before creating enhancement suggestions, please check [this list](#before-submitting-an-enhancement-suggestion) as you might find out that you don't need to create one. When you are creating an enhancement suggestion, please [include as many details as possible](#how-do-i-submit-a-good-enhancement-suggestion). Fill in [the template](ISSUE_TEMPLATE.md), including the steps that you imagine you would take if the feature you're requesting existed.

#### Before Submitting An Enhancement Suggestion

* **Determine which repository the enhancement should be suggested in**
* **Check if there's already an issue in the backlog that tracks adding that enhancement**
* **Perform a [cursory search](https://github.com/search?q=+is%3Aissue+user%3Asquanchy-dev)** to see if the enhancement has already been suggested. If it has, add a comment to the existing issue instead of opening a new one

#### How Do I Submit A (Good) Enhancement Suggestion?

Enhancement suggestions are tracked as [GitHub issues](https://guides.github.com/features/issues/). After you've determined which repository your enhancement suggestion is related to, create an issue on that repository and provide the following information:

* **Use a clear and descriptive title** for the issue to identify the suggestion
* **Provide a step-by-step description of the suggested enhancement** in as many details as possible
* **Provide specific examples to demonstrate the steps**. Include copy/pasteable snippets which you use in those examples, as [Markdown code blocks](https://help.github.com/articles/markdown-basics/#multiple-lines)
* **Describe the current behavior** and **explain which behavior you expected to see instead** and why
* **Include screenshots and animated GIFs** which help you demonstrate the steps or point out the part of Squanchy which the suggestion is related to
* **Explain why this enhancement would be useful** to most Squanchy users and isn't something that can or should be implemented
* **List some other applications where this enhancement exists** as a reference
* **Specify which version of Squanchy you're using** You can get the exact version by checking the Squanchy Settings screen
* **Specify the name and version of the OS you're using**

### Your First Code Contribution

Unsure where to begin contributing to Squanchy? You can start by looking at the backlog of the repositories and see if there is anything that looks reasonably small and manageable.

#### Local development

Squanchy can be developed locally, but requires a working Firebase instance to be connected to, in order to run. For instructions on how to do this, see the wiki of each repository. Feel free to open issues to request enhancements to the documentation if you find it lacking.

### Pull Requests

* Fill in [the required template](PULL_REQUEST_TEMPLATE.md)
* Include issue numbers in the PR title
* Include screenshots and animated GIFs in your pull request whenever possible
* Make sure you use the IDE formatter with the rules we have in the repositories to ensure a consistent coding style. When in doubt look at the rest of the codebase for inspiration, or feel free to ask the team
* Include unit tests and integration tests wherever possible
* End all files with a newline
* Ensure you follow the general architecture, style and format of the rest of the codebase

## Styleguides

### Git Commit Messages

* Use the present tense ("Add feature" not "Added feature")
* Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
* Limit the first line to 72 characters or less
* Leave the second line empty, if you need more than one line
* Reference issues and pull requests liberally after the first line
* Consider starting the commit message with an applicable emoji:
    * :art: `:art:` when improving the format/structure of the code
    * :racehorse: `:racehorse:` when improving performance
    * :non-potable_water: `:non-potable_water:` when plugging memory leaks
    * :memo: `:memo:` when writing docs
    * :bug: `:bug:` when fixing a bug
    * :fire: `:fire:` when removing code or files
    * :green_heart: `:green_heart:` when fixing the CI build
    * :white_check_mark: `:white_check_mark:` when adding tests
    * :lock: `:lock:` when dealing with security
    * :arrow_up: `:arrow_up:` when upgrading dependencies
    * :arrow_down: `:arrow_down:` when downgrading dependencies
    * :shirt: `:shirt:` when removing linter warnings

## Additional Notes

### Issue and Pull Request Labels

This section lists the labels we use to help us track and manage issues and pull requests. Most labels are used across all Squanchy repositories, but some are specific to each repository.

[GitHub search](https://help.github.com/articles/searching-issues/) makes it easy to use labels for finding groups of issues or pull requests you're interested in. For example, you might be interested in [open issues across `squanchy-dev/squanchy-android` and all Squanchy-owned packages which are labeled as bugs, but still need to be reliably reproduced](https://github.com/search?utf8=%E2%9C%93&q=is%3Aopen+is%3Aissue+user%3Asquanchy-dev+label%3Abug) or perhaps [open pull requests in `squanchy-dev/squanchy-android` which haven't been reviewed yet](https://github.com/search?utf8=%E2%9C%93&q=is%3Aopen+is%3Apr+repo%3Asquanchy-dev%2Fsquanchy-android+comments%3A0). We encourage you to read about [other search filters](https://help.github.com/articles/searching-issues/) which will help you write more focused queries.

The labels are loosely grouped by their purpose, but it's not required that every issue have a label from every group or that an issue can't have more than one label from the same group. Epics are tracked with `epic:` labels.
