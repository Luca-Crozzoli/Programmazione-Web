package com.units.it.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.units.it.utils.Consts;
import com.units.it.utils.DevException;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

public class Jwt {

    // https://github.com/auth0/java-jwt

    private static final String secret = "awnU@yn[?@MRcvn7PV/+bhm+j6t$bhX}6%+#J7rqziyv@vAWy8c:m%tQxfo=Yq8MP6]Ra.YLkty5zk[+gRkDaW8gm5t5YfVNtA6oywHN8dfB*UD?BIufv3+rv[erN.mPh7,01YjRnYDVVIXWyoIEf/+wzkp4nGn-rgN$UVfva/4QyBIuLWH,B8Pb-f?8Pk_J*aeB+d*c8f0:5R4Byr5R6oyC*k7ky2jy3j&NoD8P812HqWb3uN67aaJN+VWmnF7#gulAa+g8aqdXnrcUqK?dHbAz3uQv#PP47JDP8VwqrVj(VtaNaGbD)G[t:9zAa,Tj5x2xH$1Deyo[R8S4.yuIxeE4TlYCS*VyvjG3{nPuK:,gJUrKmnl8N/WrCe%+4W]N[y[mH3}vmc6UB/sw65Vf}2Cyd1u&pNa8XD8wFtgzF8DBd8tq5?3L7FTEQvR](!f@mdTtbLsh@AHs4$GfY?IMpK?$ddzDbvh5f5mv!rtR,c47C4jW@ckZWbqHe0cZp,pjUw.1HexdaDe8p+0=bY$msucU,OAu68!kvfaqCk6e-kGNwjWqa}/SAGQ+Kgxtk{U55ne#XDhH8EI7/U,Hgad/G=P!GmpcK8JDqX([yUDtp3WfAt0p*e7P-JC$9bv*x-nWXn0gbCPl3aR=C]a(Od.jW%Y{UqwXeKa@tRk9iNZY8IhpGXSd-Sevv9gxd@)(btnr$fjhEo96XpKxv}3/[Pkum0HE/5GB=210xVgDcbK!F!=pF2MHAe=U-SRkDnz8eK%4tjVqBk,/@C]Gexa6ZB?C5/%Ugm0kDe?h5=LVW65B?+K]i4Tsm.{guGj-8k4Nvbmg/y5(r?5r(3Q+8nnUcF0gD8/,B,dq8k2vP=uxWiLqj*Br8GVnRudx8+.yeov+nRUHyJxM8(IWpY[qu2bGsewDbLVaw8ZWcc:mm8X_dzz,dKcxj2CSv[%6e9Wvxb-PCZRHTnI+c,a{=XEuc5uycg7fdTcb)qDAmIGCyewxuHAI;muG;3U8I{T6zexYbJyWz:fS*U}Ryr)vd[?k5nPm!9PC*@USWbg0r}equcKzNz4DOW5WvvP]NAv,P)DPvKbdhkvcaAEB;TPJ$i8vh86BNFVWB7e0YU?a6LtCr}$eH&tcP{h5t3DYqrVAqlEBatGCs+FKhQ/@Guy/@cRVT8w_UfnCI.cIdmFDzjpU5JBzCaKF&Y8EMIe!*Bn}pyHyvTkaYg86c89ycchuAJRRxnjl1TCkrcK*XwGm3i*@B*$)u*Kd?r5fybiDWgXbsaT0GfPSSzXP_[HM.$cASrV{*btZM[um?28ZMWj+u3Fl;LkuyP}dHJGs[?dVdvD{#QnEPcKxT7*tX,?tzNWdbCIg@ZNh0HBK3hPstADJ32[xSBAW=fIVn*%64Im*wg,YHRs1+G?CHhElQmPnXc.mTbV7OOv%2(XfWzeyg(@Var%aQ{:kjxbgr5mT8ruJxAEvTBPVlR3g&GR-yhr5QUciAq,sLp!B?!H]?MbrVhGtZGjtUdx09UHmxOrKHB3s-u0WVwpxv:qdgm[HBz4z?F+PkIa6puHdto*7cKrdDf+VP8c7d9W6snc,acdggT+7U4F,;nci8Ane,Ro)AqB";
    private static final Algorithm algorithm = Algorithm.HMAC256(secret);

    public static String generateJWT(String username, String role) throws DevException {
        try {
            Date now = new Date();
            Calendar expiresDate = Calendar.getInstance();
            expiresDate.setTime(now);
            expiresDate.add(Calendar.DATE,1);
            Date expires = expiresDate.getTime();

            String token = JWT.create()
                    .withIssuer("Luca Crozzoli")
                    .withSubject(username)
                    .withIssuedAt(now)
                    .withExpiresAt(expires)
                    .withClaim("role", role)
                    .sign(algorithm);

            if (Consts.debug) {
                System.out.println("token generated now: "+ token);
            }

            return token;
        } catch (JWTCreationException exception) {
            System.out.println(exception.getMessage());
            throw new DevException("JWTCreationException");
        }
    }

    public static boolean verifyJWT(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Luca Crozzoli")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            if(jwt == null){
                return false;
            }else{
                return true;
            }
        } catch (JWTVerificationException exception) {
            System.out.println("Invalid signature/claims");
            return false;
        }
    }

    public static DecodedJWT decodeJWT(String token) {
        try {
            return JWT.decode(token);
        } catch (JWTDecodeException exception) {
            System.out.println("Invalid token decoded");
            return null;
        }
    }

    public static String getTokenJWTFromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }

    public static String getRoleFromJWT(String token) throws DevException {
        DecodedJWT decodedJWT = decodeJWT(token);
        if (decodedJWT == null)
            throw new DevException("Invalid token for extracting Role");
        Claim role = decodedJWT.getClaim("role");

        if(Consts.debug){
            System.out.println("Role from the JWT Token: "+ role.asString());
        }

        return role.asString();
    }

    public static String getUsernameFromJWT(String token) throws DevException {
        DecodedJWT decodedJWT = decodeJWT(token);
        if (decodedJWT == null)
            throw new DevException("Invalid Token for extracting username");

        if(Consts.debug){
            System.out.println("JWT username from the token: "+ decodedJWT.getSubject());
        }

        return decodedJWT.getSubject();
    }

}