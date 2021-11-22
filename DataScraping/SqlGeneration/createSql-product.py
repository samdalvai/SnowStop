import json
# coding: latin1

# create sql for articles
with open('../Json_Data/articles.json', encoding='utf-8') as json_file:
    data = json.load(json_file)

    out = open('../../Sql/product.sql', 'w', encoding='utf-8')
    out.write("BEGIN;" + "\n\n")
    out.write("INSERT INTO Product VALUES" + "\n")

    for p in data['articles']:
        if p['material'] == "Painted Steel":
            for x in range(1,5):
                out.write("(")
                out.write(p['article'] + str(x) + ",")
                out.write("'" + p['name'] + "'" + ",")
                out.write("'" + p['material'] + "'" + ",")
                if x == 1:
                    out.write("'" +'Red' + "'" + ",")
                elif x == 2:
                    out.write("'" +'Brown' + "'" + ",")
                elif x == 3:
                    out.write("'" +'Black' + "'" + ",")
                else:
                    out.write("'" +'Grey' + "'" + ",")

                if p['price'] != "0.00":
                    out.write(p['price'])
                else:
                    out.write('NULL')

                if p['article'] == "030420" and x == 4:
                    out.write(");" + "\n\n")
                else:
                    out.write(")," + "\n")
        else:
            out.write("(")
            out.write(p['article'] + "0,")
            out.write("'" + p['name'] + "'" + ",")
            out.write("'" + p['material'] + "'" + ",")
            out.write('NULL' + ",") # COLOR!!
            if p['price'] != "0.00":
                out.write(p['price'])
            else:
                out.write('NULL')

            if (p['article'] == "030420"):
                out.write(");" + "\n\n")
            else:
                out.write(")," + "\n")

out.write("END;")
print(out.name + " created")
out.close

