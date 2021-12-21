# Unicode-Internal-Hackathon
![u1](https://user-images.githubusercontent.com/80092236/146875567-e95eb076-1624-49e6-9333-69da6ca0856f.jpg)            ![u2](https://user-images.githubusercontent.com/80092236/146875569-cd91fd7d-973d-47a2-a2b2-d9ca3aa8dd08.jpg)

![u3](https://user-images.githubusercontent.com/80092236/146875577-0390f01f-edf5-4244-928d-fcaca9838e55.jpg)            ![u4](https://user-images.githubusercontent.com/80092236/146875596-4cdff28d-5a92-498d-aa52-24031ccb520a.jpg)     ![u6](https://user-images.githubusercontent.com/80092236/146877723-d26fa82b-d9ec-4c57-84f6-2b5d72d9b1e6.jpg)


The User can switch between registering as Buyer or Seller by clicking on the buttons at top.
The 2 ways of registering are through email-password or through phone number-OTP.
Basic user details are asked and stored in firestore.

Once registered, the user will only have to login from the below activity if they are ever logged out again

![u5](https://user-images.githubusercontent.com/80092236/146875604-9e5859d9-bf28-42b1-a860-56a017d78c3c.jpg)

<b>Registration as Buyer</b>
<br>
If you register as buyer, the following activity will be open up first.
Here the buyer can see all the products available that the sellers have put up for sale.
The buyer can also search for a particular product by name.<br>
![u11](https://user-images.githubusercontent.com/80092236/146875729-e4d26e92-276c-4141-8206-330a4ea65ab2.jpg)

Clicking on a particular product will open the following activity.
Here the buyer can see the product details and place an order by clicking on the "add requirement" button.
An Alert Dialog will open up asking for the quantity of product to be bought.
By cicking on Add in the ALert Dialog, the order will be shown to the seller.<br>
<br>By using the navigation drawer, the user can switch to the "Orders" Activity.<br>
![u13](https://user-images.githubusercontent.com/80092236/146875650-9a6dd07d-f195-4ce4-b076-c5099d7ab0c2.jpg)
<br>Here the buyer can see the orders placed by them and also the status of the order
The buyer can also search for a particular order by name.
By clicking on a particular item, the buyer will be able to see the order description.<br>
![u15](https://user-images.githubusercontent.com/80092236/146875658-2a268ff9-2654-4fe3-8af0-8c98f1e065aa.jpg)
<br>There is an option for the user to log out of their account in the navigation drawer.<br>
![u7](https://user-images.githubusercontent.com/80092236/146875613-35990c6f-ffbd-4466-b465-78cd9882c5a3.jpg)

![u9](https://user-images.githubusercontent.com/80092236/146875626-45f8cae6-2521-44cd-8749-d3b24a1e74d8.jpg)
![u10](https://user-images.githubusercontent.com/80092236/146875727-de1bbc6b-685a-4667-aa9e-cb765f5230c2.jpg)

![u12](https://user-images.githubusercontent.com/80092236/146877406-ccd5dd54-1ca5-4d0c-b646-ff661c858943.jpg)

![u14](https://user-images.githubusercontent.com/80092236/146875655-e0ece36b-cf5b-4d45-b49d-a9c8fee160b7.jpg)
![u16](https://user-images.githubusercontent.com/80092236/146875673-3f24611f-b2a1-4359-b86f-bb592a4c3e92.jpg)


<b>Registered as Seller</b><br>
When user registers as seller the user is taken to your products activity in the app .
If the user has just registered then he will be prompted with AlertDialog box where he will be asked to input minimum amount for order.
On the your products activty user can see all of his products in a recycleview which has some of the details of that product.
This includes name of product, mrp of product and discounted price of product.
A floating button is visible which can be used to add new products.
On clicking the product seller will be taken product description activity where he can update details of the prducts and also delete the product
On click of floating button a new add product activity will be opened where user will have to add details of new product which includes name, product description, 
product mrp, discounted price, minimum quantity.
There is a left nav for users using which user can open different acitvities.
A user can open orders activity from left nav where he can see all his order and some details of particular order, on clicking the order he will be taken to order details activity where he can see all the details of the order and accept or reject a particular order.
From all products in left nav user will be taken to all products activity where he can see all the listed products and some of its details, on clicking a particular product he can see all the details of the listed products which include product name, comapany name, mrp of product and min quantity of the product 
Min amount in left opens up a alertdialog box from where user can update his min amount.
Logout in left nav logs the user out of the app
