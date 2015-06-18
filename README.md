# RaspberryPiControl

3.6.1.3 Nesne Tanıma

Öncelikle renk uzayını RBG formatından Hsv’ye dönüştürülür. Opencv kütüphanesi içerisinde HSV maksimum alabilecekleri değerler bu şekildedir H = 180, S = 255, V = 255. 
Renge göre nesne tanımasını gerçekleştirirken belirli bir HSV aralığı için değerler girerek o renk değerleri arasındaki tüm nesnelerin tanınması sağlanır.
Öncelikle HSV renk uzayına dönüşümü gerçekleştirilir.

Imgproc.cvtColor(bgr, hsv, Imgproc.COLOR_RGB2HSV);

Bgr isimli görüntü matrisini hsv adlı görüntü matrisine renk uzayını HSV’ye dönüştürerek atılır.
Daha sonra hangi HSV renk aralığındaki nesneleri tanımak istediğimizin bilgisi aşağıdaki gibi verilir.
cvInRangeS(imgHSV, cvScalar(Hmin, Smin, Vmin), cvScalar(Hmax, Smax, Vmax), imgThreshold);

Bundan sonra erode ve dilate işlemlerini sırasıyla yaparak görüntü üzerinde temizlemeler yapıyoruz. Ve sonuçta elimizde tek bir renge sahip objeleri tanıyan bir yazılım olmuş oluyor.
Bu renk uzayı aralıklarını anlık olarak İstemci programından (Android, masaüstü) set edilebiliyor. 
 
Şekil 3.14 İstemci Programda Görüntü Yakalanması

Nesne tanıması gerçekleştikten sonra nesnenin kameraya olan uzaklığı fotoğraf üzerinden yaklaşık olarak hesaplanır. Ama piksel olarak işlem yapacağımız için gerçek uzaklık hesaplamalarını dikkate almıyoruz.  Ama kullanıcıyı bilgilendirme açısında yaklaşık bir gerçek uzaklık buluyoruz. Bunun için yaptığımız deneysel sonuçlardan faydalanıyoruz. Yani cismin resimdeki piksel uzaklığı ve ölçtüğümüz gerçek uzaklıklardan yola çıkıyoruz. Bu değerleri baz alarak tam bir formül çıkaramasakda yaklaşık olarak belirli bir pikselden uzak olmadığı sürece bir gerçek uzaklık değeri hesaplayabiliyoruz.


H= (Fotoğraf Karesinin Yüksekliği) – (Nesnenin Y Kordinatı)
Bu şekilde kameraya olan dik uzaklık piksel olarak hesaplandıktan sonra, robotumuz belirli bir piksel aralığı kalana kadar nesneye yaklaşmaya devam eder. Yine başlangıçta sabit olarak aldığımız bir değere göre H değerini karşılaştırır, eğer baz aldığımız değerden H değeri küçükse cisme yeterince yaklaşmışız demektir. Bundan sonra robotumuz kendini sağa yada sola doğru döndürür. Bu modülde robotumuz otomatik kendi kendini yönlendirme yapar. 
3.6.1.4 Nesne Takibi

 
Şekil 3.15 İstemci Programda Görüntü Takibi
Kameradan alınarak işlenen görüntüde istediğimiz renkteki nesneyi tesbit ettikten sonra bu nesnenin kameraya olan uzaklığını fotoğraf üzerinden piksel olarak hesaplıyoruz. Bu değer ekranda gözüktüğü gibi H değerimizi bize veriyor. Nesne takibi algoritmamızda H değerini nesnenin kamera açısından çıkmaması için kullanıyoruz. Nesne hareketli ve bu H değeri gittikçe artıyorsa nesne ileri doğru gidiyor demektir. Bu durumda robotumuz nesneyi takip etmesi gerekir. Bunun için sabit bit H değerini baz alarak nesnenin kamera açısından çıkmaması sağlanır. Görüntü anlık alındığı için eğer H değeri baz aldığımız değerden büyük ise robotumuzu ileri doğru hareket ettiririz.
H  = (Fotoğraf Karesinin Yüksekliği) – (Nesnenin Y Kordinatı)
W değerini algoritmamızda robotumuzun sağ yada sola hareketi için kullanırız. Belirli bir W değerini baz alarak anlık olarak W tekrar hesaplanır ve anlık hesaplanan W değeri baz aldığımız W değeri ile karşılaştırılır ve buna göre robotun sağa yada sola dönmesi sağlanır.
W = (Fotoğraf Karesinin Genişliği) – (Nesnenin X Kordinatı)
Eğer W değeri > 20 ise sağa dönmesi gerekir.
Eğer W değeri < Fotoğraf Karesinin Genişliği -20 ise sola dönmesi gerekir diye bir algoritma yazabiliriz.

3.6.1.5  Mesaj Protokolü

"SPEED"

Bu mesaj kodu istemci tarafından cihaza iletilir. Bu kod başlığı ile gelen mesajı kabul eden cihazımız hızını değiştirmesi gerektiğini anlar ve hızını bu mesajın içerisinde olan değerle değiştirir. Aşağıda bu mesaj koduyla gönderilen mesaja örnek bir mesaj verilmiştir.
SPEED/İstemciAdi/HızDegeri
SPEED/Android/40
"COMMAND"
Cihazımız bu komutu aldığı zaman sağa, sola, ileriye, geriye komutlarından birini çalıştıracağını anlar. Mesaj çözüldükten sonra mesaj içeriği olarak kabul ettiğimiz komut şekli okunur. Bu komutlarda sabit değer olarak kod içerisinde şu şekilde tanımlanmıştır; 
COMMAND_STOP  = "0";
COMMAND_RIGHT = "1";
COMMAND_LEFT  = "2";
COMMAND_GO    = "3";
COMMAND_BACK  = "4"; 




 Bu komut şekline göre komutu gerçekleştirir. Örnek command mesajı:
COMMAND/Android/0
"GEAR"
Bu kod başlığına sahip olan mesaj robot  tarafından kabul edilirse, mesajın içerinin true yada false olmasına göre otomatik kontrol açılır ve robot kendini kontrol etmeye başlar. Engelleri görerek onlara çarpmadan hareketine devam eder. Örnek mesaj:
GEAR/Android/true
"FOLLOW"
Robotumuz bu kod başlığına sahip olan mesajı kabul ettiği zaman, mesajın içeriğine göre nesne takibi yapmaya başlar. Örnek mesaj:
FOLLOW/Android/true
"START"
Start koduna sahip mesajı alan robot, kamera yayınını başlatacağını anlar. Ve kamera yayına başlar. Mesajın içeriğe sahip olması gerekmez. Örnek mesaj:
START/Android/nocontent

