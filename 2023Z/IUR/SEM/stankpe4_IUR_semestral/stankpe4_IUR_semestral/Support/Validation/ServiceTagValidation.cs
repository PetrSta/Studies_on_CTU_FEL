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
    internal class ServiceTagValidation : ValidationRule
    {
        public override ValidationResult Validate(object value, CultureInfo cultureInfo)
        {
            // check if service record tag can be used
            if (value is BindingExpression)
            {
                BindingExpression bindingExperssionValue = (BindingExpression)value;
                string recordTag = (bindingExperssionValue.DataItem as ServiceRecordsDataModel).RecordTag;
                if (recordTag.Length == 0)
                {
                    return new ValidationResult(false, "Record tag needs to be at least 1 char");
                }
                return ValidationResult.ValidResult;
            }
            return ValidationResult.ValidResult;
        }
    }
}
