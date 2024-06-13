# The Publication Configuration Server

The publication configuration server is designed to deliver the configuration of tenant-specific publication types. It provides a REST API, which is used by the **core software** to supply the corresponding tenant-specific forms (Publication Types) at runtime.

```mermaid

%%{
  init: {
    'theme': 'neutral'
  }
}%%

block-beta
columns 4
  pubType["Publication
type"] space
block:scope:2
configTool["Tenant
configurator
Tool"]
termsConfig["Terms
configurator
Tool"]
end
space:6
block:outOfScope:2
Config["Configuration
file"]
termDB[("
Terms
catalog")]
end


Config--"save/load"-->configTool
configTool-->Config
termDB-->configTool
outOfScope--"configures"-->pubType
termsConfig-->termDB
termDB--"save/load"-->termsConfig



style outOfScope stroke:#f66,stroke-width:2px,color:#fff,stroke-dasharray: 5 5
style scope stroke:grey,stroke-width:2px,color:#fff,stroke-dasharray: 5 5
```

${\color{red}----}$ Scope of this repository

${\color{grey}----}$ Out of scope (for the Publication Type Configurator see Repository [here](https://github.com/officialgazette/publication-configurator))

## API
Example of a tenant specific request

```
https://amtsblattportal.ch/terms/kabvs/AL-VS15
```
Structure of the tenant specific terms

https://github.com/officialgazette/publication-standard?tab=readme-ov-file#the-terminology-catalogue

## Boundary to the core software
The core software (Edoras One*) loads the configurations using a REST call. As the core software has been customized and is not available under a free license anymore, the detailed implementation will not be covered here.
