from bs4 import BeautifulSoup
import requests
import re
import time
# coding: latin1


def get_zone(province):
    if province.strip() == "Aosta" or province.strip() == "Belluno" or province.strip() == "Bergamo" or province.strip() == "Biella" or province.strip() == "Bolzano" or province.strip() == "Brescia" or province.strip() == "Como" or province.strip() == "Cuneo" or province.strip() == "Lecco" or province.strip() == "Pordenone" or province.strip() == "Sondrio" or province.strip() == "Torino" or province.strip() == "Trento" or province.strip() == "Udine" or province.strip() == "Verbania" or province.strip() == "Vercelli" or province.strip() == "Vicenza":
        return "I-A"
    elif province.strip() == "Alessandria" or province.strip() == "Ancona" or province.strip() == "Asti" or province.strip() == "Bologna" or province.strip() == "Cremona" or province.strip() == "Forlì-Cesena" or province.strip() == "Lodi" or province.strip() == "Milano" or province.strip() == "Modena," or province.strip() == "Novara" or province.strip() == "Parma" or province.strip() == "Pavia" or province.strip() == "Pesaro e Urbino" or province.strip() == "Piacenza" or province.strip() == "Ravenna" or province.strip() == "Reggio Emilia" or province.strip() == "Rimini" or province.strip() == "Treviso" or province.strip() == "Varese":
        return "I-M"
    elif province.strip() == "Arezzo" or province.strip() == "Ascoli Piceno" or province.strip() == "Bari" or province.strip() == "Campobasso" or province.strip() == "Chieti" or province.strip() == "Ferrara" or province.strip() == "Firenze" or province.strip() == "Foggia" or province.strip() == "Genova" or province.strip() == "Gorizia" or province.strip() == "Imperia" or province.strip() == "Isernia" or province.strip() == "La Spezia" or province.strip() == "Lucca" or province.strip() == "Macerata" or province.strip() == "Mantova" or province.strip() == "Massa Carrara" or province.strip() == "Padova" or province.strip() == "Perugia" or province.strip() == "Pescara" or province.strip() == "Pistoia" or province.strip() == "Prato" or province.strip() == "Rovigo" or province.strip() == "Savona" or province.strip() == "Teramo" or province.strip() == "Trieste" or province.strip() == "Venezia" or province.strip() == "Verona":
        return "II"
    elif province.strip() == "Agrigento" or province.strip() == "Avellino" or province.strip() == "Benevento" or province.strip() == "Brindisi" or province.strip() == "Cagliari" or province.strip() == "Caltanisetta" or province.strip() == "Carbonia-Iglesias" or province.strip() == "Caserta" or province.strip() == "Catania" or province.strip() == "Catanzaro" or province.strip() == "Cosenza" or province.strip() == "Crotone" or province.strip() == "Enna" or province.strip() == "Frosinone" or province.strip() == "Grosseto" or province.strip() == "L’Aquila" or province.strip() == "Latina" or province.strip() == "Lecce" or province.strip() == "Livorno" or province.strip() == "Matera" or province.strip() == "Medio Campidano" or province.strip() == "Messina" or province.strip() == "Napoli" or province.strip() == "Nuoro" or province.strip() == "Ogliastra" or province.strip() == "Olbia Tempio" or province.strip() == "Oristano" or province.strip() == "Palermo" or province.strip() == "Pisa" or province.strip() == "Potenza" or province.strip() == "Ragusa" or province.strip() == "Reggio Calabria" or province.strip() == "Rieti" or province.strip() == "Roma" or province.strip() == "Salerno" or province.strip() == "Sassari" or province.strip() == "Siena" or province.strip() == "Siracusa" or province.strip() == "Taranto" or province.strip() == "Terni" or province.strip() == "Trapani" or province.strip() == "Vibo Valentia" or province.strip() == "Viterbo":
        return "III"
    else:
        return ""

def get_load(zone):
    if zone == "I-A":
        return 1.5
    elif zone == "I-M":
        return 1.5
    if zone == "II":
        return 1
    if zone == "III":
        return 0.6
    else:
        return -1

page = requests.get("https://it.wikipedia.org/wiki/Province_d%27Italia")
soup = BeautifulSoup(page.content, 'html.parser')

items = soup.find_all("table")

out = open('./provinces.json', 'a', encoding='utf-8')
out.write('{' + "\n" + "\t" + "\"provinces\": [" + "\n")

rows = items[2].find_all('tr')


# 1 to 107

#for x in range(1,108):
 #   province = rows[x].find_all('td')
  #  #print(province)
   # name = province[1].get_text()
    #abbr = province[3].get_text()
    #print("name: " + name + " abbr: " + abbr)
    #out.write("\t" + "{" + "\n")

    #out.write("\t\t" + "\"name\": " + "\"" + name.replace("\n","") + "\"" + "," + "\n")
    #out.write("\t\t" + "\"abbr\": " + "\"" + abbr.replace("\n","") + "\"" + ","+ "\n")
    #out.write("\t\t" + "\"climatic-zone\": " + "\"" + get_zone(name) + "\"" + "," + "\n")
    #out.write("\t\t" + "\"base-load\": " + str(get_load(get_zone(name))) + "\n")

    #if name.strip() == "Viterbo":
     #   out.write("\t}" + "\n")
    #else:
     #   out.write("\t}," + "\n")

#out.write("\t" + "]" + "\n")
#out.write("}")
#out.close
