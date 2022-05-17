# Project Setup

1 - download SAP Commerce binary zip version 1905.XX

2 - extract zip under cyberporttest directory

3 - checkout source code from github under cyberporttest (merge the folders) with command _git clone git@github.com:umuttoker/cyberporttest.git_

4 - OPTINAL if you want to define a mysql db, crate db and put the credentials to project.properties. If you want to go with hsql, you do nothing

5 - change directory on terminal ${HYBRIS_HOME}/bin/platform 

6 - run (FOR MacOs, Linux) _. ./setantenv.sh_   for (Windows) _setantenv.bat_

7 - run (FOR MacOs, Linux) _ant initialize && ./hybrisserver.sh_  for (Windows) _ant initialize && /hybrisserver.bat start_


# Task Requirements


1. Set up new hybris project 
**_setup process has been done with version 1905.38_**

2. Define new item type CustomerFeedbackReview with few properties (your choice)
 **_defined under cyberporttestcore extension with properties (review, reviewTitle, rate, language, verifiedPurchase)_**

3. Relate CustomerFeedbackReview to Customer item (one customer may have multiple feedbacks) 
**_Defined under cyberporttestcore extention as oneToMany relation _**

4. Implement a service that create CustomerFeedbackReview and set a limit of max 5000 CustomerFeedbackReview per Customer
**_Implement a validate interceptor under cyberporttestcore extension with configurable upper limit_**

5. New extension to host the new code
**_cyberporttestcore, cyberporttestfacades, cyberporttestbackoffice, cyberporttestoccaddon are the extensions which i crated for this task_**

6. OCC endpoint to return all CustomerFeedbackReview for a given customer
_**CustomerFeedbackReviewController has implemented under cyberporttestoccaddon, it serves the resource from "/{baseSiteId}/users/{userId}/customerfeedbackreviews" end point.
 you can also reach the details of endpoint and test it on swagger https://localhost:9002/rest/v2/swagger-ui.html#/customer-feedback-review-controller/GetCustomerFeedbackReviews
this endpoint is secured one, you need to get token with trusted client first. you can use below credentials which are defined under cyberporttestcore extension as essential data;
clientId: trusted_client
clientSecret : secret**_

 

Do the following backoffice customization:

 

7. include all defined properties in CustomerFeedbackReview in advanced search
_**defined under cyberporttestbackoffice extension**_

8. place all defined properties in CustomerFeedbackReview in additional tab in editor area
_**defined under cyberporttestbackoffice extension with name CYBERPORT**_

9. in listviewactions remove/disable possibility to delete CustomerFeedbackReview
 _**defined under cyberporttestbackoffice extension**_