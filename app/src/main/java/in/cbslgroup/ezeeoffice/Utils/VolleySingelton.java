package in.cbslgroup.ezeeoffice.Utils;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import androidx.annotation.NonNull;

/**
 * Created by brij on 18/1/18.
 */

public class VolleySingelton {


    private static VolleySingelton volleySingelton;
    private RequestQueue requestQueue;
    private static Context mctx;



    private HurlStack mStack;

    private VolleySingelton(Context context)  {
        mctx=context;
        this.requestQueue=getRequestQueue();

    }
    public RequestQueue getRequestQueue() {
        if (requestQueue==null){

       /*     SSLSocketFactoryExtended factory = null;

            try {
                factory = new SSLSocketFactoryExtended();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
*/

           /* final SSLSocketFactoryExtended finalFactory = factory;
            mStack = new HurlStack() {
                @Override
                protected HttpURLConnection createConnection(URL url) throws IOException {
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                    try {
                        httpsURLConnection.setSSLSocketFactory(finalFactory);
                        httpsURLConnection.setRequestProperty("charset", "utf-8");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return httpsURLConnection;
                }

            };
*/


            if (mctx != null) {



               /* try{

                    CertificateFactory  cf = CertificateFactory.getInstance("X.509");

                    Log.e("Context volleysing", String.valueOf(mctx));

                    InputStream is = mctx.getResources().openRawResource(R.raw.certificate);
                    // InputStream caInput = new BufferedInputStream(is);
                    Certificate ca;
                    try {
                        ca = cf.generateCertificate(is);
                        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
                    } finally {
                        is.close();
                    }

                    String keyStoreType = KeyStore.getDefaultType();
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                    keyStore.load(null, null);
                    keyStore.setCertificateEntry("ca", ca);

                    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                    tmf.init(keyStore);

                    SSLContext sslcontext = SSLContext.getInstance("TLS");
                    sslcontext.init(null, tmf.getTrustManagers(), null);

                    mStack = new HurlStack() {
                        @Override
                        protected HttpURLConnection createConnection(URL url) throws IOException {
                            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                            try {

                                httpsURLConnection.setSSLSocketFactory(sslcontext.getSocketFactory());
                                httpsURLConnection.setRequestProperty("charset", "utf-8");
                                //httpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return httpsURLConnection;
                        }

                    };


                }

                catch (Exception e){

                   e.printStackTrace();

                }*/



            }




           // requestQueue= Volley.newRequestQueue(mctx.getApplicationContext(),new HurlStack(null, ClientSSLSocketFactory.getSocketFactory()));

            requestQueue= Volley.newRequestQueue(mctx.getApplicationContext());


        }
        return requestQueue;
    }
    public static synchronized VolleySingelton getInstance(Context context) {
        if (volleySingelton==null){
            volleySingelton=new VolleySingelton(context);
        }
        return volleySingelton;
    }
    public<T> void addToRequestQueue(@NonNull Request<T> request){

        request.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

       // request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }

}
