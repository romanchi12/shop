/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     24.12.2018 12:37:17                          */
/*==============================================================*/


drop table if exists Category;

drop table if exists `Order`;

drop table if exists OrderItem;

drop table if exists Product;

drop table if exists User;

drop table if exists UserRole;

drop table if exists Warehouse;

/*==============================================================*/
/* Table: Category                                              */
/*==============================================================*/
create table Category
(
   CategoryId           bigint not null,
   CategoryName         varchar(256),
   primary key (CategoryId)
);

/*==============================================================*/
/* Table: `Order`                                               */
/*==============================================================*/
create table `Order`
(
   OrderId              bigint not null,
   UserId               bigint not null,
   OrderStatus          int,
   primary key (OrderId)
);

/*==============================================================*/
/* Table: OrderItem                                             */
/*==============================================================*/
create table OrderItem
(
   OrderItemId          bigint not null,
   ProductId            bigint not null,
   OrderId              bigint not null,
   OrderItemQuantity    decimal,
   primary key (OrderItemId)
);

/*==============================================================*/
/* Table: Product                                               */
/*==============================================================*/
create table Product
(
   ProductId            bigint not null,
   WarehouseItemId      bigint not null,
   CategoryId           bigint not null,
   ProductName          varchar(256),
   ProductDescription   text,
   ProductPrice         decimal,
   ProductImageSrc      varchar(256),
   primary key (ProductId)
);

/*==============================================================*/
/* Table: User                                                  */
/*==============================================================*/
create table User
(
   UserId               bigint not null,
   UserRoleId           bigint not null,
   UserName             varchar(256),
   UserSurname          varchar(256),
   UserEmail            varchar(512),
   UserPassword         varchar(512),
   primary key (UserId)
);

/*==============================================================*/
/* Table: UserRole                                              */
/*==============================================================*/
create table UserRole
(
   UserRoleId           bigint not null,
   UserRoleName         varchar(256),
   primary key (UserRoleId)
);

/*==============================================================*/
/* Table: Warehouse                                             */
/*==============================================================*/
create table Warehouse
(
   WarehouseItemId      bigint not null,
   WarehouseItemQuantity decimal,
   primary key (WarehouseItemId)
);

alter table `Order` add constraint FK_OrderUserRelationship foreign key (UserId)
      references User (UserId) on delete restrict on update restrict;

alter table OrderItem add constraint FK_OrderItemOrderRelationship foreign key (OrderId)
      references `Order` (OrderId) on delete restrict on update restrict;

alter table OrderItem add constraint FK_ProductOrderItemRelationship foreign key (ProductId)
      references Product (ProductId) on delete restrict on update restrict;

alter table Product add constraint FK_CategoryProductRelationship foreign key (CategoryId)
      references Category (CategoryId) on delete restrict on update restrict;

alter table Product add constraint FK_ProductWarehouseRelationship foreign key (WarehouseItemId)
      references Warehouse (WarehouseItemId) on delete restrict on update restrict;

alter table User add constraint FK_UserUserRoleRelationship foreign key (UserRoleId)
      references UserRole (UserRoleId) on delete restrict on update restrict;

