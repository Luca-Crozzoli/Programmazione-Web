package com.units.it.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.units.it.utils.Consts;
import com.units.it.utils.DevException;

public class Token {

    private static final String secret = "QXs7CpfWQ0!c,tJuaW=]o?p6?y8Itv+fAsqg89zVaXH!5kmR&BTWAbc]KX{SY%8m7)%54R5z=8&%rVqxkhoYLnyk1@x5w89AYavh#aoDcc6;3+yXvel2nXc(R5!8iVJEdjIp6X/&pI?/8+,JR!5#TU(?9R$@t6VyMNggTv,u#NpJ0rF;Tq3zm0a!MhWr=zR6AG1.GdhuCtNklryExbkC?K45I/m2CVBR586D#rTIX}tm:N7&(d]pv8xQY}@LRzon(bXh6pb++mt5+fAXp-YrhbsRBcj,[34E:jHQp0jX-DptdDjVBVWkF]auiGX9UYcSFeDk0b(Fo;bg$}m2.y[WKzzkqKw2u$lB%Q}7cFhMLUw!xGk9t/[Z1,zhozw7gAB!7MyvEBiZuK50(8GLpyo1T*av3bRN5h+t&tGvzY!VtmPNy(lvpVBai+7:}vTtrWKy-H6j3buIKbU[6&OM0qjSB{XG}!gH*qbWrRCRkwLs;NdBYhYnR;W,Ur-BsxkCKC0-W8Z//Gi=SNBsma+x[ds#wZ7P0kAFVeCfHE{rHfm[c7b8Hm6Vdw588/mbZ:68zRm5T3!.SHY5rOxkJbFxUc%cEBlri93VB)+rWa5AY3&NjE$&jE-OhVR=?Wg5DU_N]$wFXD@Hahg}VyXR+hBJg77!xphDxu5zC533?IB85p%ja0CsM2fD[,c+rNT9GIDTh:x{cfTV,Yg2jF,khvvP?/Qy@XGZ=dgtAw3DaTTFU/D3YJFQM?kX2XvnYo/hw=dB2C3rGJBDD(RYWDWXlJd;ID5PnDJ8r[[)IAg(naKpQna2+C4g7gMqmgpac!KL6XppQboahiwDt;,;jQ_tedewtzmuM%v6XkUtTtrCdgn7B0;O..evfP=3.HrFjp6_zJML1RjgM]-dthE.*Dci66+a&0lSWCTR]n8tbwCYdh?TBQ4%qP!Wk=3rqoww{pVJDZ4BD4L?wUWymueZawq*gklzURCOJHX4@P1VbDlimsBF,VcKf5HswgIvBRKK{(Qd&P-8AkZ*QRhbnxH[dV8BJ,%9:kKkUhehK2M6@k{+saK/3Zf8)Lf&nKBNH3t7OCUj33[cAgAMY/tck1KVS!snydeAh8xdTu{J?xPG#d;DQP%C!7zAwzwmHQF0e%ZyWrtjbfYUy7EH[WyPp:0f;UieD3VHG_{x%te{p4lQvrC6d(:hfQVa0GCA-zPiXFkc?cZxW[3X,cKsfwTrMJVt5gwk8GfwqV+#3lEsFGcay8H6V2D76#pO]ageSklw7gP@jRIR,wDQ7[6_4B{yTB*TwE#hn[}uDB22eNT3gtvatBKP2DYnDpx)vM#$IKBa%-xrYzNY]5DHUUnD;WATFcs32NjtjR/TnAHBm]RAk8Hd#?).Ga(Ns7Z}ju25t0mQ4VB#&FX0rJu?@y]4/q@vtHgb=+npxc4FFPHLh,!Miq:)+WtaxvfjYq?,D&7NTNF4p**v3WW{6;8mR(ejfKyJ6BJbtJzc3&x+c.f/Tp-XJV6PdmY-jAt4HRZHWEDdF/a,*Oibye4E6cWsoUt2c.JrMVpfNC4?JH(4CWy]dL7hupZuDDMp+t4WcKgyjDJ+dUHXKUT8My2Lb{kB,$a%(Iv";
    private static final Algorithm algorithm = Algorithm.HMAC256(secret);

    public static String generateToken(String fileId) throws DevException {
        try {
            String token = JWT.create()
                    .withIssuer("Luca Crozzoli")
                    .withSubject(fileId)
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

    public static boolean verifyToken(String token, String fileId) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Luca Crozzoli")
                    .withSubject(fileId)
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

}
