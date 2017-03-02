import csv
import numpy as np
from sklearn.preprocessing import LabelEncoder
from sklearn.preprocessing import OneHotEncoder
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cross_validation import train_test_split
from sklearn import metrics
import pydot
from sklearn import tree

with open('CompleteTraningFile.csv', 'r') as csvfile:
    titanic_reader = csv.reader(csvfile, delimiter=';', quotechar='"')

    corpus = []
    row = next(titanic_reader)
    feature_names = np.array(row)

    # Load dataset, and target classes
    titanic_X, titanic_y = [], []
    for row in titanic_reader:
        corpus.append(row[4])
        titanic_X.append(row)
        titanic_y.append(row[8])  # The target value is "AgriTweet"

    titanic_X = np.array(titanic_X)
    titanic_y = np.array(titanic_y)
    print (feature_names, titanic_X[0], titanic_y[0])
    print corpus

titanic_X = titanic_X[:, [0, 1, 3, 4, 5]]
feature_names = feature_names[[0, 1, 3, 4, 5]]
print (feature_names)
print (titanic_X[12], titanic_y[12])


lbl_enc = LabelEncoder()
lbl_enc.fit(titanic_X[:, 0])
print ("Categorical classes:", lbl_enc.classes_)
integer_classes = lbl_enc.transform(lbl_enc.classes_).reshape(15, 1)
print ("Integer classes:", integer_classes)
enc = OneHotEncoder()
one_hot_encoder = enc.fit(integer_classes)
# First, convert clases to 0-(N-1) integers using label_encoder
num_of_rows = titanic_X.shape[0]
t = lbl_enc.transform(titanic_X[:, 0]).reshape(num_of_rows, 1)
# Second, create a sparse matrix with three columns, each one indicating if the instance belongs to the class
new_features = one_hot_encoder.transform(t)
# Add the new features to titanix_X
titanic_X = np.concatenate([titanic_X, new_features.toarray()], axis=1)
# Eliminate converted columns
titanic_X = np.delete(titanic_X, [0], 1)
# Update feature names
feature_names = ['username', 'date', 'retweets', 'hashtags', 'Downy Mildew', 'Early Blight', 'Fusarium',
                 'Head Blight', 'Late Blight', 'Leaf Blight', 'Leaf Curl',
                 'Leaf Rust', 'Leaf spot', 'Powdery Mildew', 'Root rot', 'Septoria',
                 'Snow Mold', 'Yellow Rust']

print "******************cat label*******************"
lbl_enc = LabelEncoder()
lbl_enc.fit(titanic_y)
print ("Categorical classes:", lbl_enc.classes_)
integer_classes = lbl_enc.transform(lbl_enc.classes_).reshape(7, 1)
print ("Integer classes:", integer_classes)
enc = OneHotEncoder()
one_hot_encoder = enc.fit(integer_classes)
# First, convert clases to 0-(N-1) integers using label_encoder
num_of_rows = titanic_y.shape[0]
t = lbl_enc.transform(titanic_y).reshape(num_of_rows, 1)
# Second, create a sparse matrix with three columns, each one indicating if the instance belongs to the class
new_features = one_hot_encoder.transform(t)
# Add the new features to titanix_X
titanic_y = np.concatenate([new_features.toarray()], axis=1)
# Eliminate converted columns
titanic_y = np.delete(titanic_y, [0], 1)




tfv = TfidfVectorizer()
titanic_X = tfv.fit_transform(corpus)
# text_data = list(titanic_X.apply(lambda x: '%s %s %s' % (x[1], x[4], x[5]), axis=1))

titanic_X = titanic_X.astype(float)
titanic_y = titanic_y.astype(float)
X_train, X_test, y_train, y_test = train_test_split(titanic_X, titanic_y, test_size=0.25, random_state=33)
# from sklearn import tree
clf = tree.DecisionTreeClassifier(max_depth=3,min_samples_leaf=5)
clf = clf.fit(X_train,y_train)


def measure_performance(X, y, clf, show_accuracy=True, show_classification_report=True, show_confusion_matrix=True):
    y_pred = clf.predict(X)
    # if show_accuracy:
    #     print ("Accuracy:{0:.3f}".format(metrics.accuracy_score(y, y_pred)), "\n")

    if show_classification_report:
        print ("Classification report")
        print (metrics.classification_report(y, y_pred), "\n")

    if show_confusion_matrix:
        print ("Confusion matrix")
        print (metrics.confusion_matrix(y, y_pred), "\n")


# measure_performance(X_train, y_train, clf, show_classification_report=False, show_confusion_matrix=False)

clf_dt = tree.DecisionTreeRegressor(max_depth=3,min_samples_leaf=5)
clf_dt.fit(X_train,y_train)
measure_performance(X_test,y_test,clf_dt)
tree.export_graphviz(clf_dt, out_file='tree.dot')