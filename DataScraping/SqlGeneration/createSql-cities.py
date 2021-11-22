import json
# coding: latin1

# create sql for cities
with open('../Json_Data/cities.json', encoding='utf-8') as json_file:
    data = json.load(json_file)

    out = open('../../Sql/city.sql', 'w', encoding='utf-8')
    out.write("BEGIN;" + "\n\n")
    out.write("INSERT INTO City VALUES" + "\n\n")

    for p in data['cities']:
        # SKIP CITIES WITH NULL CAP AND PROVINCE
        if p['cap'] != "NULL" and p['province'] != "NULL":  
            name = p['name'].replace("'","''").replace("à","a").replace("ò","o").replace("è","e").replace("ù","u").replace("ì","i").replace("a''","a").replace("o''","o").replace("e''","e").replace("u''","u").replace("i''","i")

            out.write("(")
            out.write("'" + p['cap'] + "'" + ",")
            out.write("'" + name.upper() + "'" + ",")
            out.write("'" + p['province'] + "'" +  ",")
            if p['altitude'] == -1:
                out.write(str(0))
            else:
                out.write(str(p['altitude']))

            if (p['name'] == "Villaspeciosa"):
                out.write(");" + "\n\n")
            else:
                out.write(")," + "\n")

out.write("END;")
print(out.name + " created")
out.close

# create sql for provinces
with open('../Json_Data/provinces.json', encoding='utf-8') as json_file:
    data = json.load(json_file)

    out = open('../../Sql/province.sql', 'w', encoding='utf-8')
    out.write("BEGIN;" + "\n\n")
    out.write("INSERT INTO Province VALUES" + "\n")

    for p in data['provinces']:
        name = p['name'].replace("'","''").upper()   

        out.write("(")
        out.write("'" + p['abbr'] + "'" + ",")
        out.write("'" + name + "'" + ",")
        out.write("'" + p['climatic-zone'] + "'" +  ",")
        out.write(str(p['base-load']))
        if (p['name'] == "Viterbo"):
            out.write(");" + "\n\n")
        else:
                out.write(")," + "\n")

out.write("END;")
print(out.name + " created")
out.close