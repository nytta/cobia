# **cobia**

## TODO

- develop <cobia:registry /> tab

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

