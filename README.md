# **cobia**

## TODO

- develop registry type:Zookeeper Registry in tag <cobia:registry />

## cobia tag

### <cobia:service />

| name |optional | type | description|
| --- | ----- | ----- | --- |
|interface|required|string|Interface implemented by service|
|ref|required|string|bean id of service|
|registry|optional|string|registry address|

### <cobia:reference />

| name |optional | type | description|
| --- | ----- | ----- | --- |
|id|required|string|id of this reference|
|interface|required|string|Interface of reference|
|registry|optional|string|registry method:direct, zookeeper|
|serviceServer|optional|string|`serviceServer` value is provider address(host:port) when `registry` is direct|

### <cobia:registry />

| name |optional | type | description|
| --- | ----- | ----- | --- |
|type|required|string| value(direct, zookeeper) of registry type |
|address|optional|string|address of registry|

