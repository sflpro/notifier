Contributing
============

1. Fork the repo, develop and test your code changes, add docs.
2. Make sure that your commit messages clearly describe the changes.
3. Send a pull request (please follow [gitflow](https://nvie.com/posts/a-successful-git-branching-model/) guidelines).


Here are some guidelines for hacking on sflpro/notifier.


Using maven for build/test
--------------------------
After you clone the repository use Maven for building and running the tests. 
Integration tests run tests against dockerized services.
Docker v17+ is required.
Maven 3.0+ is required.


Known issue: no Known issues yet :) .

**Please, do not use your production projects for executing integration tests.** While we do our best to make our tests independent of your project's state and content, they do perform create, modify and deletes, and you do not want to have your production data accidentally modified.

Adding Features
---------------
In order to add a feature to  sflpro/notifier:

The feature must be fully documented using Javadoc and examples should be provided.
The feature must work fully on Java 8 and above.
The sonar checks should be passed.
The feature must not add unnecessary dependencies (where "unnecessary" is of course subjective,
but new dependencies should be discussed).

Coding Style
------------
Maintain the coding style in the project and in particular the modified files.