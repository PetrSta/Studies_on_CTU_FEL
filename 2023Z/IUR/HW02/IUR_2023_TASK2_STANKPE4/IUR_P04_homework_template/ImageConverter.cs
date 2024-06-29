using System;
using System.Globalization;
using System.IO;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Media.Imaging;

namespace IUR_P04_homework_template
{
    public class ImageConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            try
            {
                string selectedImage = string.Empty;
                ComboBoxItem comboItem = (ComboBoxItem)value;
                string comboBoxContent = (string)comboItem.Content;
                
                if(comboBoxContent == "Muž")
                {
                    selectedImage = "Men";
                }
                else
                {
                    selectedImage = "Woman";
                }

                string currentDirectory = System.IO.Path.GetFullPath(@"..\..\..\");
                string path = string.Format(@"{0}\Images\{1}.png", currentDirectory, selectedImage);
                return new BitmapImage(new Uri(path));
            }
            catch (Exception exception)
            {
                return null;
            }
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
