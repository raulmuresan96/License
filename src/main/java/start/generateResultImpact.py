import sys
from reportlab.platypus import Spacer
from reportlab.platypus import KeepTogether
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.lib import colors
from reportlab.lib.pagesizes import letter, inch, A4
from reportlab.platypus import Image, Paragraph, SimpleDocTemplate, Table, TableStyle
from reportlab.lib.styles import getSampleStyleSheet

totalCitationsScore = 0.0
cCitationsScore = 0.0

def buildCategoryTable(article, citedList, categoryList, punctajList, data, tableStyle):
    #global totalCitationsScore
    print("NUmar de lucrari" + str(len(citedList)))
    firstRow = len(data)
    j = 0
    # if j == 0:
    #     data[firstRow][0] = Paragraph(text="<para alignment=left><font color=black size=8>" + article + "</font>",style=styles["Normal"])
    # else:
    #     data[j][0] = ''
    #data[firstRow][0] = Paragraph(text="<para alignment=left><font color=black size=8>" + article + "</font>",style=styles["Normal"])

    while j < len(citedList):
        jAtBeginning = j

        #font name = Arial

        #round(punctajList[k], 2)

        for k in range(j, min(len(citedList), j + 5)):
            print k
            p1 = Paragraph(text="<para alignment=left><font name = Arial color=black size=8>" + citedList[k] + "</font>",
                           style=styles["Normal"])
            p2 = Paragraph(text="<para alignment=center><font name = Arial color=black size=8>" + str(categoryList[k]) + "</font>",
                           style=styles["Normal"])
            p3 = Paragraph(text="<para alignment=center><font name = Arial color=black size=8>" + str(round(float(punctajList[k]), 2)) + "</font>",
                           style=styles["Normal"])
            #totalCitationsScore += punctajList[k]
            data.append(['', p1, p2, p3])
        print ('new line')
        tableStyle.add('SPAN', (0, jAtBeginning), (0, j))


        j = j + 5

    # for i in range(0, len(citedList)):
    #     p1 = Paragraph(text="<para alignment=left><font color=black size=8>" + citedList[i] + "</font>",
    #                    style=styles["Normal"])
    #     p2 = Paragraph(text="<para alignment=center><font color=black size=8>" + str(categoryList[i]) + "</font>",
    #                    style=styles["Normal"])
    #     p3 = Paragraph(text="<para alignment=center><font color=black size=8>" + str(punctajList[i]) + "</font>",
    #                    style=styles["Normal"])
    #     #list = ['', p1, p2, p3]
    #     # list.append(KeepTogether(''))
    #     # list.append(KeepTogether(p1))
    #     # list.append(KeepTogether(p2))
    #     # list.append(KeepTogether(p3))
    #     #data.append(['', KeepTogether(list)])
    #     data.append(['', p2, p3])
    #     #data.append(KeepTogether(['', p1, p2, p3]))
    #     #data.append(['', citedList[i], categoryList[i], punctajList[i]])
    # if len(citedList) == 0:
    #     data.append(['', '', '', ''])
    data[firstRow][0] = Paragraph(text="<para alignment=left><font name = Arial color=black size=8>" + article + "</font>",style=styles["Normal"])
    lastColumn = firstRow + max(0, len(citedList) - 1)
    #tableStyle.add('SPAN', (0, firstRow), (0, lastColumn))
    tableStyle.add('LINEBELOW', (0, lastColumn), (-1, lastColumn), 1, colors.black)


    #reportDocContent.append(KeepTogether(Paragraph(commentParagraph, style=commentParagraphStyle)))




def parseSingleCitation(string):
    list = string.split("#")
    print('Title ' + list[0])
    print('Category ' + list[1])
    print('Points ' + list[2])



def parsePublicationAndCitation(string, data, tableStyle):
    global totalCitationsScore
    global cCitationsScore
    list = string.split("=")
    publication = list[0]
    citations = list[1]
    print(publication)

    citationsTitle = []
    citationsCategory = []
    citationsPoints = []

    for citation in citations.split("@"):
        #parseSingleCitation(citation)
        citationFields = citation.split("#")
        citationsTitle.append(citationFields[0])
        citationsCategory.append(citationFields[1])
        citationsPoints.append(citationFields[2])
        totalCitationsScore += float(citationFields[2])
        # print('TIP!!!!!!!')
        # print(type(citationFields[1]))
        if citationFields[1] == 'C':
            cCitationsScore += float(citationFields[2])
    #print citationsTitle
    #print citationsCategory
    #print citationsPoints
    buildCategoryTable(publication, citationsTitle, citationsCategory, citationsPoints, data, tableStyle)

    print('&&&&&&punctaje')
    print(citationsPoints)





    #parseCitations(citation)
    #print(citation)


print('Second Python Script')



pdfmetrics.registerFont(TTFont('Verdana', 'Verdana.ttf'))
pdfmetrics.registerFont(TTFont("Verdana-Bold", "verdanab.ttf"))
pdfmetrics.registerFont(TTFont("Verdana-Italic", "Verdana Italic.ttf"))
pdfmetrics.registerFont(TTFont("Arial", "Arial.ttf"))

doc = SimpleDocTemplate("src/main/resources/SecondPDF.pdf", pagesize=A4)
# container for the 'Flowable' objects
elements = []

styleSheet = getSampleStyleSheet()
styles = getSampleStyleSheet()

def generateFirstTable(name, punctaj1, punctaj2):
    #str(round(punctaj2,2))

    p1 = Paragraph(text=  "<font color=white>Nume Prenume: " + name + "</font>",  style=styles["Normal"])
    p2 = Paragraph(text=  "<font color=white>Impactul rezultatelor: </font>",  style=styles["Normal"])
    p3 = Paragraph(text=  "<font color=black>Punctaj total citari</font>",  style=styles["Normal"])
    p4 = Paragraph(text=  "<para alignment=right><font color=black>" + str(round(punctaj1,2)) + " puncte</font>",  style=styles["Normal"])

    p5 = Paragraph(text=  "<font color=black>Punctaj citari din forumuti de tip A si B</font>",  style=styles["Normal"])
    p6 = Paragraph(text=  "<para alignment=right><font color=black >" + str(round(punctaj2,2)) + " puncte</font>",  style=styles["Normal"])

    lucrariABC = "Lucrari categoriile A, B si C"

    data = [[p1 , 'a'] ,[p2, 'b'], [p3, p4], [p5, p6]]

    colWidth = [200, 100]
    t = Table(data, colWidths=colWidth, rowHeights=None,
              style=[
                  #('ALIGN', (0, 0), (0, 0), 'LEFT'),
                  ('VALIGN', (0, 0), (-1, -1),'MIDDLE'),
                  ('SPAN', (0, 0), (1, 0)),
                  ('SPAN', (0, 1), (1, 1)),
                  ('BOX', (0, 0), (-1, -1), 1.2, colors.black),

                  #('ALIGN', (1, 2), (1, 2), 'CENTER'),
                  #('LINEBELOW', (0, 0), (-1, -1), 0.8, colors.black),
                  #('GRID', (0, 0), (-1, -1), 0.5, colors.black),
                  #('BACKGROUND', (1, 1), (1, 2), colors.lavender),
                  ('BACKGROUND', (0, 0), (1, 1), colors.Color(red=(115.0/255),green=(115.0/255),blue=(115.0/255)))
              ],
              hAlign='LEFT')
    return t



p1 = Paragraph(text=  "<para alignment=center><font color=white>Lucrare citata</font>",  style=styles["Normal"])
p2 = Paragraph(text=  "<para alignment=center><font color=white>Citari</font>",  style=styles["Normal"])
p3 = Paragraph(text=  "<para alignment=center><font color=white>Categorie CNATDCU</font>",  style=styles["Normal"])
p4 = Paragraph(text=  "<para alignment=center><font color=white>Punctaj</font>",  style=styles["Normal"])

# # lucrariABC = "Lucrari categoriile A, B si C"
# articleList = ["abcd", "xyzt", "qwerty"]
# punctajList = [100, 50, 73]

data = [[p1 , p2, p3, p4] ]

tableStyle = TableStyle(
    [('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),
     # ('SPAN', (0, 0), (1, 0)),
     ('BOX', (0, 0), (-1, -1), 1.2, colors.black),
     ('LINEBELOW', (1, 1), (-1, -1), 0.1, colors.black),
     # ('GRID', (0, 0), (-1, -1), 0.5, colors.black),
     ('LINEAFTER', (0, 0), (0, -1), 0.8, colors.black),
     ('ALIGN', (1, 1), (-1, -1), 'CENTER'),
     # ('BACKGROUND', (1, 1), (1, 2), colors.lavender),
     ('BACKGROUND', (0, 0), (-1, 0), colors.Color(red=(115.0 / 255), green=(115.0 / 255), blue=(115.0 / 255)))
     ]
)

input = sys.argv[1].replace('$', ' ')
list = input.split("%")
for i in list:
    parsePublicationAndCitation(i, data, tableStyle)


colWidth = [100, 200, 70, 60]
t = Table(data, colWidths=colWidth, rowHeights=None,
          style=tableStyle,
          hAlign='LEFT')



authorName = sys.argv[2].replace("?", " ")



elements.append(generateFirstTable(authorName, round(totalCitationsScore, 2), round(totalCitationsScore - cCitationsScore, 2)))
elements.append(KeepTogether(Spacer(1, 24)))
elements.append(t)

# write the document to disk

#buildCategoryTable("Gabi's first article", ["cited1", "cited2", "cited3"], ["A*", "A", "B"], [10, 20, 30], data, tableStyle)


doc.build(elements)

print(totalCitationsScore)


# publication = list[0];
# #print(publication.split("%"))
# print("!!!!!!")
# for i in publication.split("%"):
#     list2 = input.split("=")
#     print (list2[0])
#     parseCitations(list2[1])
