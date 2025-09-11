package org.example.haikangweishi.num1;

/**
 * @author yjz
 * @Date 2025/9/7 16:35
 * @Description
 *
 */
public class Singleton {
    
    private static volatile Singleton instance;
    
    private Singleton(){}
    
    public static Singleton getInstance(){
        if(instance == null){
            synchronized (Singleton.class){
                if(instance == null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
