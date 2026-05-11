package org.example.model;

class Rate {
    float rate;
    int usersCount;

    public Rate(int rate, int usersCount) {
        this.rate = rate;
        this.usersCount = usersCount;
    }


    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }
}

