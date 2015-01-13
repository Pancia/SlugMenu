package com.dayzerostudio.slugguide.slugmenu.menu.menuobjects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JsonMenuObject {

    public final static int BREAKFAST = 0;
    public final static int LUNCH = 1;
    public final static int DINNER = 2;

    @SerializedName("menu")
    public Menu menu = new Menu();
    @SerializedName("server")
    public Server server = new Server();
    @SerializedName("request")
    public Request request = new Request();
    @SerializedName("response")
    public Response response = new Response();

    public String toString() {
        return "{" + this.menu.toString() + ", " +
                this.server.toString() + ", " +
                this.request.toString() + ", " +
                this.response.toString() + "}";
        }


    public class Menu {
        @SerializedName("breakfast")
        private ArrayList<String> breakfast;
        @SerializedName("lunch")
        private ArrayList<String> lunch;
        @SerializedName("dinner")
        private ArrayList<String> dinner;
        @SerializedName("dh")
        private String dh;

        public String toString() {
            String ret = "\"menu\": { \"dh\": " + getDh();
            if (hasBreakfast())
                ret += ", \"breakfast\": " + getBreakfast();
            if (hasLunch())
                ret += ", \"lunch\": " + getLunch();
            if (hasDinner())
                ret += ", \"dinner\": " + getDinner();
            ret += "}";
            return ret;
        }

        public String getDh() {return "\""+dh+"\"";}
        public void setDh(String dh) {this.dh = dh;}

        public boolean hasBreakfast() {return breakfast!=null;}
        public String getBreakfast() {return toValidJsonArray(breakfast);}
        public void setBreakfast(ArrayList<String> breakfast) {this.breakfast = breakfast;}

        public boolean hasLunch() {return lunch!=null;}
        public String getLunch() {return toValidJsonArray(lunch);}
        public void setLunch(ArrayList<String> lunch) {this.lunch = lunch;}

        public boolean hasDinner() {return dinner!=null;}
        public String getDinner() {return toValidJsonArray(dinner);}
        public void setDinner(ArrayList<String> dinner) {this.dinner = dinner;}

        private String toValidJsonArray(ArrayList<String> array) {
            String jsonArray = "[";
            for (String str : array) {
                jsonArray += "\""+str+"\", ";
            }
            jsonArray = jsonArray.substring(0, jsonArray.length()-2) + "]";
            return jsonArray;
        }

        public ArrayList<MenuItem> getMeal(int i) {
            switch (i) {
                case BREAKFAST:
                    return toMenuItemList(breakfast);
                case LUNCH:
                    return toMenuItemList(lunch);
                case DINNER:
                    return toMenuItemList(dinner);
                default:
                    return null;
            }
        }

        private ArrayList<MenuItem> toMenuItemList(ArrayList<String> items) {
            if (items == null || items.isEmpty())
                return null;
            ArrayList<MenuItem> itemList = new ArrayList<MenuItem>();
            for (String item : items) {
                itemList.add(new MenuItem(item));
            }
            return itemList;
        }

        public void setMealWith(String meal, ArrayList<String> mealsMenu) {
            if (meal.equals("Breakfast")) {
                setBreakfast(mealsMenu);
            } else if (meal.equals("Lunch")) {
                setLunch(mealsMenu);
            } else if (meal.equals("Dinner")) {
                setDinner(mealsMenu);
            }
        }
    }
    public class Server {
        @SerializedName("error")
        private int error;
        @SerializedName("warning")
        private int warning;
        public String toString() {return "\"server\": { \"error\": " + getError() + ", \"warning\": " + getWarning() + "}";}

        public int getError() {return error;}
        public void setError(int error) {this.error = error;}

        public int getWarning() {return warning;}
        public void setWarning(int warning) {this.warning = warning;}
    }
    public class Request {
        @SerializedName("success")
        private int success;
        public String toString() {return "\"request\": { \"success\": " + getSuccess() + "}";}

        public boolean wasSuccessful() {
            return getSuccess()==1;
        }

        public int getSuccess() {return success;}
        public void setSuccess(int success) {this.success = success;}
    }
    public class Response {
        @SerializedName("message")
        private String message;
        @SerializedName("title")
        private String title;
        public String toString() {return "\"response\": { \"message\": " + getMessage() + ", \"title\": " + getTitle() + "}";}

        public String getMessage() {return "\""+message+"\"";}
        public void setMessage(String message) {this.message = message;}

        public String getTitle() {return "\""+title+"\"";}
        public void setTitle(String title) {this.title = title;}
    }

}
