# **KLASHA ASSESSMENT**

* This application runs on http://localhost:8080
* For hands n testing, the swagger url can be accessed fro http://localhost:8080/swagger

#### **Documentation**
All endpoints follow a request mapping of http://localhost:8080/klasha/{endpoint}

* Get highest population endpoint 
GET http://localhost:8080/klasha/filter/{limit} takes a Path Variable of limit, returns a JSON body.

* Get State ans Cities endpoint GET http://localhost:8080/klasha/stateAndCities/{country} takes a Path Variable of country, returns a JSON body.

* Get Country data endpoint GET http://localhost:8080/klasha/data/{country} takes a Path Variable of country, returns a JSON body.

* Convert currency endpoint POST http://localhost:8080/klasha/getRate takesa Request Body {
  "country": {Desired Country},
  "currency": {target currency},
  "amount": {amount}
  } returns a JSON body.