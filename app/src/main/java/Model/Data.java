package Model;

import java.util.Objects;

public class Data {
    String item, desc, time, randomId;
    int price;

    public Data(String item, String desc, int price, String time, String randomId) {
        this.item = item;
        this.desc = desc;
        this.price = price;
        this.time = time;
        this.randomId = randomId;
    }

    public Data() {}

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRandomId() {
        return randomId;
    }

    public void setRandomId(String randomId) {
        this.randomId = randomId;
    }
}
