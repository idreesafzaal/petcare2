package petcare.idreesafzaal.petcare;

import com.google.gson.annotations.SerializedName;

public class getdoctorregResponse {
    @SerializedName("code")
    String Code;

    @SerializedName("doctor_id")
    String Doctor_id;

    public String getCode() {
        return Code;
    }

    public String getDoctor_id() {
        return Doctor_id;
    }
}
