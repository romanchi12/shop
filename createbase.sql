/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     09.01.2019 17:00:13                          */
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
   CategoryId           bigint not null auto_increment,
   CategoryName         varchar(256),
   primary key (CategoryId)
);

/*==============================================================*/
/* Table: `Order`                                               */
/*==============================================================*/
create table `Order`
(
   OrderId              bigint not null auto_increment,
   UserId               bigint not null,
   OrderStatus          int,
   SummaryPrice         decimal(15,2),
   primary key (OrderId)
);

/*==============================================================*/
/* Table: OrderItem                                             */
/*==============================================================*/
create table OrderItem
(
   ProductId            bigint not null,
   OrderId              bigint not null,
   OrderItemQuantity    decimal
);

/*==============================================================*/
/* Table: Product                                               */
/*==============================================================*/
create table Product
(
   ProductId            bigint not null auto_increment,
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
   UserId               bigint not null auto_increment,
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
   UserRoleId           bigint not null auto_increment,
   UserRoleName         varchar(256),
   primary key (UserRoleId)
);

/*==============================================================*/
/* Table: Warehouse                                             */
/*==============================================================*/
create table Warehouse
(
   WarehouseItemId      bigint not null auto_increment,
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

