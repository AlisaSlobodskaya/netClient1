# The client part of the network console chat

At startup, it opens a permanent connection to the server,
asks the user for his name, and registers it on the server. After that, it starts receiving text messages from the user and sending them to the server. It also receives messages from other participants from the server and outputs them to the console.

Available commands:
* connect
* send [message]
* disconnect
* delete account

Simple Messaging Protocol: <br>
`GS value GS value RS value`

GS and RS are ASCII characters 1D and 1E.<br>
The first value is always the package type, such as " T_REGISTER " or "T_MESSAGE".