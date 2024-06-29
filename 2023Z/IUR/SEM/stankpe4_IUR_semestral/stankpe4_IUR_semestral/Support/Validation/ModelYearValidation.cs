using SemesterWork___car_data_database.Models;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;
using System.Windows.Data;

namespace SemesterWork___car_data_database.Support
{
    internal class ModelYearValidation : ValidationRule
    {
        public override ValidationResult Validate(object value, CultureInfo cultureInfo)
        {
            // check if year can be used for our needs
            int currentYear = DateTime.Now.Date.Year;
            string message = string.Format("Model year must be between 1886 and {0}", currentYear);

            if (value == null || value == string.Empty)
            {
                return new ValidationResult(false, message);
            } 
            else
            {
                int modelYear = int.Parse((string)value);
                if (modelYear < 1886 || modelYear > currentYear)
                {
                    return new ValidationResult(false, message);
                }
            }

            return ValidationResult.ValidResult;
        }
    }
}
