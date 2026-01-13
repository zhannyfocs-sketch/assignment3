package Entities;

public class Car {
   private int id;
   private static int idGen;
   private String make;
   private String model;
   private boolean isAvialable;

   public Car(String make, String model, boolean isAvialable) {
        id = idGen;
        idGen++;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
       this.id = id;
    }
    public String getMake() {
       return make;
    }
    public void setMake(String make) {
       if (make == null || make.isEmpty()) {
           throw new IllegalArgumentException("Make cannot be empty");
       }
       this.make = make;
    }
    public String getModel() {
       return model;
    }
    public void setModel(String model) {
       if (model == null || model.isEmpty()) {
           throw new IllegalArgumentException("Model cannot be empty");
       }
       this.model = model;
    }
    public boolean isAvialable() {
       return isAvialable;
    }
    public void setAvialable(boolean isAvialable) {
       this.isAvialable = isAvialable;
    }
    }
