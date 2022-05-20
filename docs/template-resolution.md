# Template resolution strategies

General intro

## Local Templates Resolution Strategy

Description of this approach

Configuration example
```yaml
notifier:
  templates:
    externalUrl: false
    path: file:/templates
    extension: .ftl
```

### File naming

Template naming patters

### Configuring local templates' resolution strategy for dockerized environments

Paths to override and docker command examples

## External Service Templates Resolution Strategy

Description of this approach

Configuration example 
```yaml
notifier:
  templates:
    externalUrl: true
    path: https://example.com
    extension: .ftl
```

### External Service API Specification

Implement the following API endpoints, assuming .ftl template extension:

    GET /<template_name>.ftl
        Returns the template content.

Endpoints should return the following responses:

    200 OK
        The FTL template content with Content-type: text.
    404 Not Found
        The template does not exist.