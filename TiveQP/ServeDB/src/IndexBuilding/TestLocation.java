package IndexBuilding;

public class TestLocation {
    public static void main(String[] args) throws Exception {
        String[] data = "Financial Services**ATLANTA**33.887451**-84.4678335**9**0**17**0".split("\\*\\*");



        String[] encode = new Owner().OwnerLocation(data[1],Double.parseDouble(data[2]),Double.parseDouble(data[3]));
        MyUtil.Show.showString_list(encode);

        String[] user = new User().UserLocation(data[1],Double.parseDouble(data[2]),Double.parseDouble(data[3]));
        MyUtil.Show.showString_list(user);
    }
}
