package org.romanchi;

import org.romanchi.database.entities.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public enum  Message {

    BAD_PARAMETERS("Bad parameters","Плохие параметры", "Погані параметри"),
    NO_SUCH_CATEGORY_EXIST("No such category exist", "Такой категории не существует", "Такої категорії не існує"),
    NOT_ENOUGHT_PRODUCTS_IN_STOCK("Not enough products in stock","Недостаточно товара на складе","Недостатня кількість товару на складі"),
    ADDED("Added","Добавлено","Додано"),
    FIRST_YOU_NEED_TO_LOGIN("First you need to login","Нужен вход","Потрібно увійти"),
    DELETED("Deleted","Удалено","Видалено"),
    NO_ORDERS_OPENED("No orders opened","Нет открытых заказов","Не відкрито жодних замовлень"),
    NO_ORDER_ITEM("No such order item","Нет такой позиции заказа","Такого елемента замовлення немає"),
    SERVER_ERROR("Server error", "Ошибка сервера", "Помилка серверу"),
    SAVED("Saved","Сохранено","Збережено"),
    NO_SUCH_PRODUCT("No such product","Нет такого продукта","Такого продукту немає");



    Message(String en, String ru, String uk) {
        this.en = en;
        this.ru = ru;
        this.uk = uk;
    }

    String en;
    String ru;
    String uk;
    public String getLocalized(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return this.en;
        }
        if(user.getUserLanguage()==null){
            return this.en;
        }
        switch (user.getUserLanguage()){
            case "en_GB":{
                return this.en;
            }
            case "ru_RU":{
                return this.ru;
            }
            case "uk_UA":{
                return this.uk;
            }
            default:{
                return this.en;
            }
        }
    }
}
