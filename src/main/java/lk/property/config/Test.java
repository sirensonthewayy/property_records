package lk.property.config;

public class Test {
    public static void main(String[] args) {
        StringBuilder pathOfClass = new StringBuilder(SpringConfig.class
                .getProtectionDomain().getCodeSource().getLocation().getPath());
/*        for(int i = 0; i < 3; i++){
            pathOfClass = new StringBuilder(pathOfClass.substring(0, pathOfClass.lastIndexOf("/")));
        }*/
        //pathOfClass.append("/resources/static/css/");
        System.out.println("file:/" + pathOfClass);
    }
}
