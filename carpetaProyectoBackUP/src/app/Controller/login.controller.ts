import { Sesion } from '../componentesHome/sesion/Models/sesion';
import { UserModel } from '../Models/userModel';
export class LoginController {

    private temp: Array<any>;
    private acceso: Array<any>;
    public userLogin: UserModel;
    constructor() {
    }
    public sesion: Sesion;
    comprobarUser(values: UserModel): any[] {
        this.userLogin = new UserModel(
            values.idUsuarios,
            values.nombreUsuario,
            values.apellidoUsuario,
            values.emailUsuario,
            values.passwordUsuario,
            values.tienda,
            values.creacionUsuario,
            values.fechaLogin,
            values.turnosLaborales,
            values.cedulaCiudadania,
            values.tipoUsuario,
            values.inventario);
        
        if (this.userLogin !== null) {
            console.log(this.userLogin);
            this.acceso = [{ tipo: this.userLogin.tipoUsuario, acceso: true, user: this.userLogin.emailUsuario }];
            return this.acceso;
        } else {
            console.log(this.userLogin);
            this.acceso = [{ tipo: null, acceso: false, user: null }];
            return this.acceso;
        }
    }
}