package dmit2015.model;

public class Circle {

    private double radius;

    public double getRadius(){
        return radius;
    }

    public void setRadius(double radius){
        this.radius=radius;
    }

    public Circle(){
        radius = 1;
    }

    public double area(){
        return Math.PI * Math.pow(radius,2);
    }

    public static void main(String[] arg){
        Circle currentCircle= new Circle();
        currentCircle.setRadius(5);

        System.out.printf( "Radius: %.2f, Area= %.2f\n", currentCircle.getRadius(), currentCircle.area());
    }
}
