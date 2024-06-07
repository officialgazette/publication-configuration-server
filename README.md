# The publication configuration server

```mermaid

%%{
  init: {
    'theme': 'neutral'
  }
}%%

block-beta
columns 4
  pubType["Publication
Type"] space
block:scope:2
configTool["Tenant
Configurator
Tool"]
termsConfig["Terms
Configurator
Tool"]
end
space:6
block:outOfScope:2
Config["Config
file"]
termDB[("
Term
Database")]
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

${\color{grey}----}$ Out of scope (for the Publication Configuration Tool see Repository [here](https://github.com/officialgazette/publication-configurator))
