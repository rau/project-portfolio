import pyowm
import keyring

owm = pyowm.OWM(keyring.get_password("pyowm", "api"))
reg = owm.city_id_registry()
print(reg.ids_for(""))
