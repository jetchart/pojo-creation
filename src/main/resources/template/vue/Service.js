import {BaseIdService} from './BaseIdService.js'
import {AXIOS} from '../../js/utils/http-utils-ENVIRONMENT'

export const ENDPOINT = "/delitos";

export const ::name::Service = {
    baseActions() {
        var o = Object.create(BaseIdService);
        o.setEndpoint(ENDPOINT);
        return o;
    },
}
