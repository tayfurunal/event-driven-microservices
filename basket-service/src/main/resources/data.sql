insert into basket_status(id, name, description)values (1, 'ACTIVE', 'Active');
insert into basket_status(id, name, description)values (2, 'CANCELLED', 'Cancelled');
insert into basket_status(id, name, description)values (3, 'COMPLETED', 'Completed');

insert into product(external_id, name)values ('1', 'paper');

insert into basket(status_id, user_id)values (1, 4444);
insert into basket(status_id, user_id)values (1, 1111);
insert into basket(status_id, user_id)values (1, 9999);
insert into basket(status_id, user_id)values (1, 8888);
insert into basket(status_id, user_id)values (1, 6666);
insert into basket(status_id, user_id)values (2, 3333);
insert into basket(status_id, user_id)values (3, 2222);

insert into basket_product(basket_id, price, product_id, quantity)values (1, 50, 1, 1);
insert into basket_product(basket_id, price, product_id, quantity)values (2, 50, 1, 1);
insert into basket_product(basket_id, price, product_id, quantity)values (3, 50, 1, 1);
insert into basket_product(basket_id, price, product_id, quantity)values (4, 50, 1, 1);
insert into basket_product(basket_id, price, product_id, quantity)values (5, 50, 1, 1);
insert into basket_product(basket_id, price, product_id, quantity)values (6, 12, 1, 1);
insert into basket_product(basket_id, price, product_id, quantity)values (7, 13, 1, 1);