package cordova.plugin.who.is.calling;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class whoiscalling extends CordovaPlugin {

    TelephonyManager telephonyManager;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        // Bu fonksiyon 3 parametre alıyor.
        // action, java script tarafından çağırılan fonksiyonun/actionın ismini taşıyor.
        // callbackContext ise java script ile aramızda köprü kuruyor ve içerisinde diğer tarafla iletişim kurabileceğimiz callback fonksiyonlarını barındırıyor.
        // args ise android tarafına göndermek istediğimiz parametreleri taşıyor.

        if (action.equals("registerCallEvents")) {
            //java script tarafında registerCallEvents fonksiyonunu tetiklediğimizde telefonun çağrı eventleri dinlenecek
            //bir çağrı bilgisi/durumu algılandığında java scripti callbackContext aracılığıyla bilgilendireceğiz.

            this.registerCallEvents(callbackContext);
            return true;
        }
        return false;
    }

    private void registerCallEvents(final CallbackContext callbackContext) {

      final Activity activity = this.cordova.getActivity(); // Toast mesajı göstermek için activity değişkeni oluşturuldu.

      // Çağrı bilgilerini yönetecek manager tanımlanıyor.
      telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

      // Çağrı durumlarını dinleyecek listener tanımlanıyor.
      // Kısaca bahsetmem gerekirse;
      // Telefonun ringing,offhook ve idle olmak üzere üç ayrı durumu var.
      // Bir değişiklik sezildiğinde onCallStateChanged fonksiyunu tetikleniyor.

      PhoneStateListener callStateListener = new PhoneStateListener() {
          public void onCallStateChanged(int state, String incomingNumber)
          {
              if(state==TelephonyManager.CALL_STATE_RINGING){
                  Toast.makeText(activity,"Telefon çalıyor...", Toast.LENGTH_SHORT).show();

                  // java scripte bilgi göndermek için callbackContext e atanan success fonksiyonu tetikleniyor.
                  callbackContext.success(incomingNumber + " arıyor...");

              }
              if(state==TelephonyManager.CALL_STATE_OFFHOOK){
                  Toast.makeText(activity,"Bir görüşme yapılıyor.", Toast.LENGTH_SHORT).show();
                  callbackContext.success("Görüşme başladı.");
              }

              if(state==TelephonyManager.CALL_STATE_IDLE){
                  Toast.makeText(activity,"Ne bir çağrı ne bir görüşme var.",Toast.LENGTH_SHORT).show();

                  callbackContext.success("Görüşme yok.");
              }
          }
      };

      telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);
    }
}
