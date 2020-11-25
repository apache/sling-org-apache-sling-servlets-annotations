Integration tests for the servlets annotations module.

To run or debug the tests from this folder, use:

  mvn clean verify -Dannotations.bundle.version=VVV

Where VVV is the version of the annotations bundle to test.

These could be simplified using a verify script for the
generated annotations, as in the sling-org-apache-sling-adapter-annotations
module.

Also, the ServletRegistrationIT should move to the servlets
resolver module, as it's really testing it and not just
our annotations.
