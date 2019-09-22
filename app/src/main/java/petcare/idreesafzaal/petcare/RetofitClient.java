package petcare.idreesafzaal.petcare;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetofitClient {
    public static final String BaseUrl ="http://192.168.10.37/andriodfiles/";
    private static RetofitClient mInstance;
    private Retrofit retrofit;
    private RetofitClient(){
        retrofit=new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    protected static synchronized RetofitClient getInstance(){
        if(mInstance==null){
            mInstance=new RetofitClient();
        }
        return mInstance;
    }
    public Api getApi(){
        return retrofit.create(Api.class);
    }
    public getdoctorregResponse getModel(){
        return retrofit.create(getdoctorregResponse.class);
    }
}
