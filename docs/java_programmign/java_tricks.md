# use Properties as configure file
http://zhaowenbinmail.blog.163.com/blog/static/3908086200811160347677/

我们开发JAVA应用程序时常常有一些配置信息需要进行设置，因此我们可以使用JDK中的Properties类来实现。下面我通过一个实例来说明，看代码如下：

    public class Main {
        
        private Properties properties =null;  //定义类成员，其类型为Properties

        //处理配置文件的函数，在此函数中会去读取配置文件（我这里设定配置文件名为main.properties，当然读者可以自行设定，并且可以做到更加灵活的动态实现），如果此配置文件不存在，则系统将创建这个配置文件，并将其设置一些默认的配置信息。
        public void initPropertiesFile() throws Exception {
            File f=new File("main.properties");

            String current_path=f.getCanonicalPath();           //获得当前目录路径
            System.out.println("Current Path:"+current_path);
            if(!(f.exists())){
                System.out.println("需要装载的"+current_path+"文件不存在,正在创建此文件...");

                boolean cnf_flag=f.createNewFile();                 //如果不存在，创建配置文件
                if(cnf_flag){
                    byte[] s_end={'\r','\n'};                             //用于换行
                    //以下是写入的信息，对应配置文件其信息格式是key=value格式
                    String ini_driver_properties="driverClassName=com.mysql.jdbc.Driver";
                    String ini_url_properties="url=jdbc:mysql://192.168.0.222:3306/netbar5";
                    String ini_user_properties="username=admin";
                    String ini_psw_properties="password=123456";

                    FileOutputStream fos=new FileOutputStream(f);       //建立文件写入对象
                    //以下是写入文件内容
                    fos.write(ini_driver_properties.getBytes());
                    fos.write(s_end);
                    fos.write(ini_url_properties.getBytes());
                    fos.write(s_end);
                    fos.write(ini_user_properties.getBytes());
                    fos.write(s_end);
                    fos.write(ini_psw_properties.getBytes());
                    fos.write(s_end);
                    fos.close();

                    System.out.println("创建"+current_path+"文件成功");
                }
                else{
                    System.out.println("创建"+current_path+"文件失败");
                    System.exit(1);
                }

            }
            properties = new Properties();         //创建Properties对象
            FileInputStream fis=new FileInputStream(f);    //创建输入流对象
            properties.load(fis);                     //将输入流中的数据装入Properties对象中
            fis.close();                                 //关闭输入流对象

        }

        //读取Properties对象中的值
        public String getProperty(String key){
            return properties.getProperty(key);
        }
        
        /**
         * @param args the command line arguments
         */
        public static void main(String[] args) {
            // TODO code application logic here
            Main m=new Main();
            try{
                m.initPropertiesFile();

                String driverclassname=m.getProperty("driverClassName");
                String url=m.getProperty("url");
                String username=m.getProperty("username");
                String password=m.getProperty("password");
                System.out.println("DriverClassName Property Value:"+driverclassname);
                System.out.println("URL Property Value:"+url);
                System.out.println("UserName Property Value:"+username);
                System.out.println("Password Property Value:"+password);

                System.in.read();
            }
            catch(Exception e){
                System.out.println("Current Error:"+e.getMessage());
            }
            finally{
                System.exit(1);
            }
            
        }

    }
