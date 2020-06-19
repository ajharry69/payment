# Payment
An android app showing how to integrate [stripe][stripe-url], [square][square-url], [braintree][braintree-url] and [M-Pesa][mpesa-url] payment gateways.

The project relies on [django-xpay][django-xpay-url](a [django][django-url] project) for backend code. To successfully run the app, you will need to:

1. Clone both projects(this and django-xpay)
2. Open root folder of cloned django-xpay and run the following commands in terminal:
   1. `pipenv run python manage.py migrate`. More info can be found [here][django-migrate-url].
   2. `pipenv run python manage.py createsuperuser`. More info can be found [here][django-createsuperuser-url].
   3. `pipenv run python manage.py runserver`. More info can be found [here][django-runserver-url]. Make sure it is running in port `8000` otherwise you will have to modify the frontend code's [base-url][frontend-base-url].
3. Run the and the android app.

[stripe-url]: https://stripe.com/
[square-url]: https://squareup.com/
[braintree-url]: https://www.braintreepayments.com/
[mpesa-url]: https://www.safaricom.co.ke/personal/m-pesa
[django-xpay-url]: https://github.com/ajharry69/django-xpay
[django-url]: https://www.djangoproject.com/
[django-migrate-url]: https://docs.djangoproject.com/en/3.0/ref/django-admin/#django-admin-migrate
[django-createsuperuser-url]: https://docs.djangoproject.com/en/3.0/ref/django-admin/#createsuperuser
[django-runserver-url]: https://docs.djangoproject.com/en/3.0/ref/django-admin/#runserver
[frontend-base-url]: app/src/main/java/com/xently/payment/WebServiceBuilder.kt