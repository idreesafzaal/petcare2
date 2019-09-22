package petcare.idreesafzaal.petcare;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {


    @FormUrlEncoded
    @POST("reg_doctor.php")
    Call<getdoctorregResponse> doctorsignup(
       @Field("name") String name,
       @Field("email") String email,
       @Field("info") String info,
       @Field("password") String password,
       @Field("country") String country,
       @Field("address") String address,
       @Field("number") String number,
       @Field("med_numb") String md_l
    );

//    @FormUrlEncoded
//    @POST("cvvvv")
//    Call<ResponseBody> login(
//            @Field("name") String nm,
//            @Field("password") String password
//    );
}
