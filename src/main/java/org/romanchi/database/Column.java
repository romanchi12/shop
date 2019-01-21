package org.romanchi.database;

public class Column {
    //User
    public static final String USER_USER_ID = "User.UserId";
    public static final String USER_USERNAME = "User.UserName";
    public static final String USER_USERSURNAME = "User.UserSurname";
    public static final String USER_USEREMAIL = "User.UserEmail";
    public static final String USER_USERPASSWORD = "User.UserPassword";
    public static final String USER_USERUSERROLE_ID = "User.UserRoleId";
    public static final String USER_USERADDRESS = "User.UserAddress";
    public static final String USER_USERLANGUAGE = "User.UserLanguage";

    //UserRole
    public static final String USERROLE_USERROLE_ID = "UserRole.UserRoleId";
    public static final String USERROLE_USERROLENAME = "UserRole.UserRoleName";

    //Category
    public static final String CATEGORY_CATEGORY_ID = "Category.CategoryId";
    public static final String CATEGORY_CATEGORYNAME = "Category.CategoryName";

    //WarehouseItem
    public static final String WAREHOUSE_WAREHOUSEITEM_ID = "WarehouseItem.WarehouseItemId";
    public static final String WAREHOUSE_WAREHOUSE_ITEM_QUANTITY = "WarehouseItem.WarehouseItemQuantity";

    //Product
    public static final String PRODUCT_PRODUCT_ID = "Product.ProductId";
    public static final String PRODUCT_WAREHOUSEITEM_ID = "Product.WarehouseItemId";
    public static final String PRODUCT_CATEGORY_ID = "Product.CategoryId";
    public static final String PRODUCT_PRODUCT_NAME = "Product.ProductName";
    public static final String PRODUCT_PRODUCT_DESCRIPTION = "Product.ProductDescription";
    public static final String PRODUCT_PRODUCT_PRICE = "Product.ProductPrice";
    public static final String PRODUCT_PRODUCT_IMAGESRC = "Product.ProductImageSrc";

    public static final String ORDER_ORDER_ID = "Order.OrderId";
    public static final String ORDER_USER_ID = "Order.UserId";
    public static final String ORDER_ORDER_STATUS = "Order.OrderStatus";
    public static final String ORDER_SUMMARY_PRICE = "Order.SummaryPrice";

    public static final String ORDERITEM_PRODUCT_ID = "OrderItem.ProductId";
    public static final String ORDERITEM_ORDER_ID = "OrderItem.OrderId";
    public static final String ORDERITEM_ORDER_ITEM_QUANTITY = "OrderItem.OrderItemQuantity";




}
