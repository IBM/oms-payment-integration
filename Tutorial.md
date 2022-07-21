<h1>Tutorial</h1>
A quick tutorial on how to consume oms-payment adapter.

<h2>Step 1 : Register with Adyen and get the api key</h2>

Register a [test account](https://www.adyen.com/signup) with Adyen.Each request made to Adyen is precessed through an API credential which is linked to the test account.
More on generating the api key [here](https://docs.adyen.com/development-resources/api-credentials#generate-api-key).

<h2>Step 2 : Fork this repository and import the code to an IDE</h2>

In order to customise/consume this adapter , you need to first [fork](https://docs.github.com/en/get-started/quickstart/fork-a-repo) it and then clone it .

Example : 
````bash
$ git clone https://github.com/<your_username_here>/oms-payments-adapter
````

Once done , import the contents to an IDE(preferably Intellij).

<h2>Step 3:Map the client object model to adapters object model</h2>

For the oms-payment adapter to work with any OMS/Payment service , the clients are required to adopt to their model to adapters model.
In other words , the input and output's of the adapter are **PaymentCollectionInput** and **PaymentCollectionOutput** respectively, a mapper needs 
to be written to map oms's input and output objects.

Example mapper required for IBM OMS:

| IBM OMS Objects                      	| Adapters object         	| Comments                                                            	|
|--------------------------------------	|-------------------------	|---------------------------------------------------------------------	|
| YFSExtnPaymentCollectionInputStruct  	| PaymentCollectionInput  	| Map YFSExtnPaymentCollectionInputStruct to PaymentCollectionInput   	|
| YFSExtnPaymentCollectionOutputStruct 	| PaymentCollectionOutput 	| Map YFSExtnPaymentCollectionOutputStruct to PaymentCollectionOutput 	|


Example mapper required for Sample code:

| Sample Objects        	| Adapters object         	| Comments                                                            	|
|-----------------------	|-------------------------	|---------------------------------------------------------------------	|
| ExternalPaymentInput  	| PaymentCollectionInput  	| Map ExternalPaymentInput to PaymentCollectionInput                  	|
| ExternalPaymentoutput 	| PaymentCollectionOutput 	| Map YFSExtnPaymentCollectionOutputStruct to PaymentCollectionOutput 	|


To understand what **PaymentCollectionInput** and **PaymentCollectionOutput** mean, refer **javadocs**.

The mandatory attributes required to be mapped are mentioned in **com.ibm.adapter.sample.mapper.PaymentMapper.java** file.This adapter uses mapstruct
library for mapping using **@Mapping** annotation.

````java
@Mapper
public interface PaymentMapper {
    PaymentMapper MAPPER = Mappers.getMapper(PaymentMapper.class);
    @Mapping(source = "authorizationId", target = "authorizationId")
    @Mapping(constant = "IBMAccount480ECOM", target = "merchantId")
    @Mapping(source = "authorizationId", target = "paymentReference")
    @Mapping(source = "paymentReference6", target = "paymentReference2")
    @Mapping(source = "paymentReference7", target = "paymentReference3")
    @Mapping(source = "orderNo", target = "orderNo")
    @Mapping(ignore = true, target = "paymentKey")
    @Mapping(source = "chargeType", target = "transactionType")
    @Mapping(source = "requestAmount", target = "requestAmount")
    @Mapping(source = "currentAuthorizationAmount", target = "authorizedAmount")
    PaymentCollectionInput omsToPayment(ExternalPaymentInput externalPaymentInput);


    @Mapping(source = "paymentReference", target = "PaymentReference5")
    @Mapping(constant = "true", target = "asynchRequestProcess")
    @Mapping(source = "asyncReqId", target = "asyncRequestIdentifier")
    ExternalPaymentoutput PaymentToOms(PaymentCollectionOutput paymentCollectionOutput);
}
````

<h2>Step 4: Implement the IPaymentProcessingAdapter interface</h2>

This interface contains most important methods required by the adapter. Refer **javadocs** and **com.ibm.adapter.sample.AdyenAdapter.java**
to understand more.

<h2> Step 5: Code to invoke the adapter </h2>

Invoke the adapter from OMS/payment service. The point of invocation should be during every:

* Charge (Also referred to as Capture)
* Reverse authorisation
* Amend existing authorisation
* Refund
* Re-authorization

````java
public class SampleImplementation {
    /**
     * Initialize adyen adapter
     */
    IPaymentProcessingAdapter adyenAdapter=AdyenAdapter.getAdapter(PaymentServiceProvider.ADYEN.name());


    /**
     * Convert to adapters input and invoke adapter
     * @param externalPaymentInput
     * @return
     */
    ExternalPaymentoutput collectionCreditCard(ExternalPaymentInput externalPaymentInput){

    //Use RequestContext to pass context data to adapter if required.
    //It can also be used to override business logic to skip capture,auth,refund calls
    RequestContext ctx=MockHelper.getRequestContext();

    //Invoke call adapter
    ExternalPaymentoutput externalPaymentoutput= (ExternalPaymentoutput) adyenAdapter.execute(ctx,externalPaymentInput);

    return externalPaymentoutput;
    }

}
````

<h2> Step 6: Package and install the adapter </h2>

Create a jar of the project by executing maven package command.

````bash
$ mvn clean package
````

Once the package is complete , you will find the jar under **adapter-build** module.

````
.
|-- assembly.xml
|-- pom.xml
`-- target
|-- adapter-build-1.0-SNAPSHOT-jars-with-all-modules.jar
`-- adapter-build-1.0-SNAPSHOT.jar
````
The jar **adapter-build-1.0-SNAPSHOT-jars-with-all-modules.jar** contains the code from all the modules of oms-payment-integration project. Install this jar on 
the order management system.

<h2>On consuming notifications from Adyen</h2>

Consuming notifications via webhook is out of scope. But a brief note will be provided here.
Adyen pushes the payment processing updates for each transaction via webhook notifications.

An example notification message :
````json
{
  "amount" : {
    "value" : 40000,
    "currency" : "USD"
  },
  "eventCode" : "AUTHORISATION",
  "eventDate" : 1655975157000,
  "merchantAccountCode" : "IBMAccount480ECOM",
  "merchantReference" : "for test payment",
  "originalReference" : null,
  "pspReference" : "W2RB8SS4XCQ2WN82",
  "reason" : "088608:0008:03/2030",
  "success" : true,
  "paymentMethod" : "mc",
  "operations" : [ "CANCEL", "CAPTURE", "REFUND" ]
}
````

Each notification message has an attribute "success" and its value can be **true** or **false**.
Depending on the value update the status of the transaction in the OMS.

